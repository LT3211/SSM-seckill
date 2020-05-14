package com.lt.mapper;


import com.lt.entity.Seckill;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface SeckillMapper {

    /**
     * 查询所有秒杀商品的记录
     * @return
     */
    List<Seckill> findAll();

    /**
     * 根据主键查询当前秒杀商品的数据
     * @param seckillId
     * @return
     */
    Seckill findById(@Param("seckillId") long seckillId);

    /**
     * 减库存。
     * 对于Mapper映射接口方法中存在多个参数的要加@Param()注解标识字段名称，不然Mybatis不能识别出来哪个字段相互对应
     *
     * @param seckillId 秒杀商品ID
     * @param killTime  秒杀时间
     * @return 返回此SQL更新的记录数，如果>=1表示更新成功
     */
    int reduceStock(@Param("seckillId") long seckillId, @Param("killTime") Date killTime);

}
