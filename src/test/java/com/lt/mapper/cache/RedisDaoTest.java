package com.lt.mapper.cache;

import com.lt.entity.Seckill;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;

import static org.junit.Assert.*;

/**
 * 配置spring和junit整合，junit启动时加载SpringIOC容器
 * spring-test，junit
 */
@RunWith(SpringJUnit4ClassRunner.class)
//告诉junit spring配置文件
@ContextConfiguration({"classpath:spring/spring-dao.xml"})
public class RedisDaoTest {

    @Autowired
    private RedisDao redisDao;

    @Test
    public void getSeckill() {
        Seckill seckill = redisDao.getSeckill(123456l);
        System.out.println(seckill);
    }

    @Test
    public void putSeckill() {
        Seckill seckill=new Seckill();
        seckill.setSeckillId(123456l);
        seckill.setTitle("测试jedis");
        seckill.setCreateTime(new Date());
        redisDao.putSeckill(seckill);
    }
}