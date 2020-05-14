package com.lt.service.impl;

import com.lt.dto.Exposer;
import com.lt.dto.SeckillExecution;
import com.lt.entity.Seckill;
import com.lt.entity.SeckillOrder;
import com.lt.enums.SeckillStatEnum;
import com.lt.exception.RepeatKillException;
import com.lt.exception.SeckillCloseException;
import com.lt.exception.SeckillException;
import com.lt.mapper.SeckillMapper;
import com.lt.mapper.SeckillOrderMapper;
import com.lt.mapper.cache.RedisDao;
import com.lt.service.SeckillService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Service
public class SeckillServiceImpl implements SeckillService {

    //日志API
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    //设置盐值字符串，随便定义，用于混淆MD5值
    private final String salt = "sjajaspu-i-2jrfm;sd";

    @Autowired
    private SeckillMapper seckillMapper;

    @Autowired
    private SeckillOrderMapper seckillOrderMapper;

    @Autowired
    private RedisDao redisDao;

    @Override
    public List<Seckill> findAll() {
        List<Seckill> seckillList = seckillMapper.findAll();
        return seckillList;
    }

    @Override
    public Seckill findById(long seckillId) {
        return seckillMapper.findById(seckillId);
    }

    /**
     * 暴露秒杀接口
     * 使用redis优化后获取SeckillUrl的方法
     *
     * @param seckillId
     * @return
     */
    @Override
    public Exposer exportSeckillUrl(long seckillId) {
        //优化点:缓存优化——超时的基础上维护一致性
        //1:访问redis
        Seckill seckill = redisDao.getSeckill(seckillId);
        if (seckill == null) {
            //2:访问数据库
            seckill = seckillMapper.findById(seckillId);
            if (seckill == null) {
                return new Exposer(false, seckillId);
            } else {
                //3:放入redis
                redisDao.putSeckill(seckill);
            }
        }


        Date startTime = seckill.getStartTime();
        Date endTime = seckill.getEndTime();
        //获取系统时间
        Date nowTime = new Date();
        if (nowTime.getTime() < startTime.getTime() || nowTime.getTime() > endTime.getTime()) {
            return new Exposer(false, seckillId, nowTime.getTime(), startTime.getTime(), endTime.getTime());
        }
        //转换特定字符串的过程，不可逆的算法
        String md5 = getMD5(seckillId);
        return new Exposer(true, md5, seckillId);
    }

    //生成MD5值
    private String getMD5(Long seckillId) {
        String base = seckillId + "/" + salt;
        String md5 = DigestUtils.md5DigestAsHex(base.getBytes());
        return md5;
    }

    /**
     * 使用注解式事务方法的有优点：开发团队达成了一致约定，明确标注事务方法的编程风格
     * 使用事务控制需要注意：
     * 1.保证事务方法的执行时间尽可能短，不要穿插其他网络操作PRC/HTTP请求（可以将这些请求剥离出来）
     * 2.不是所有的方法都需要事务控制，如只有一条修改的操作、只读操作等是不需要进行事务控制的
     * 3.Spring默认只对运行期异常进行事务的回滚操作，对于编译异常Spring是不进行回滚的，所以对于需要进行事务控制的方法尽可能将可能抛出的异常都转换成运行期异常
     */
    @Override
    public SeckillExecution executeSeckill(long seckillId, BigDecimal money, long userPhone, String md5) throws SeckillException, RepeatKillException, SeckillCloseException {
        if (md5 == null || !md5.equals(getMD5(seckillId))) {
            throw new SeckillException("seckill data rewrite");
        }

        //执行秒杀逻辑:1.减库存；2.储存秒杀订单
        Date nowTime = new Date();
        try {
            //记录秒杀订单信息
            int insertCount = seckillOrderMapper.insertOrder(seckillId, money, userPhone);
            //唯一性：seckillId,userPhone，保证一个用户只能秒杀一件商品
            if (insertCount <= 0) {
                //重复秒杀
                throw new RepeatKillException("seckill repeated");
            } else {
                //减库存，热点商品竞争
                int updateCount = seckillMapper.reduceStock(seckillId, nowTime);
                if (updateCount <= 0) {
                    //没有更新到记录,秒杀结束，rollback
                    throw new SeckillCloseException("seckill is closed");
                } else {
                    //秒杀成功
                    SeckillOrder seckillOrder = seckillOrderMapper.findById(seckillId, userPhone);

                    //更新缓存(更新库存数量)
                    Seckill seckill = redisDao.getSeckill(seckillId);
                    seckill.setStockCount(seckill.getStockCount() - 1);
                    redisDao.putSeckill(seckill);

                    return new SeckillExecution(seckillId, SeckillStatEnum.SUCCESS, seckillOrder);
                }
            }
        } catch (SeckillCloseException e) {
            throw e;
        } catch (RepeatKillException e) {
            throw e;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            //所有编译器异常转换为运行期异常
            throw new SeckillException("seckill inner error:" + e.getMessage());
        }
    }
}
