package com.lt.mapper;

import com.lt.entity.SeckillOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.math.BigDecimal;

import static org.junit.Assert.*;

/**
 * 配置spring和junit整合，junit启动时加载SpringIOC容器
 * spring-test，junit
 */
@RunWith(SpringJUnit4ClassRunner.class)
//告诉junit spring配置文件
@ContextConfiguration({"classpath:spring/spring-dao.xml"})
public class SeckillOrderMapperTest {

    @Autowired
    private SeckillOrderMapper seckillOrderMapper;

    @Test
    public void insertOrder() {
        int i = seckillOrderMapper.insertOrder(1l, BigDecimal.valueOf(120.00), 17857324868L);
        System.out.println(i);
    }

    @Test
    public void findById() {
        SeckillOrder seckillOrder = seckillOrderMapper.findById(1l, 17857324868L);
        System.out.println(seckillOrder.getSeckillId() + ": " + seckillOrder.getSeckill().getTitle());
    }
}