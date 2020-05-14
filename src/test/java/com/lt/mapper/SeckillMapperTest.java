package com.lt.mapper;

import com.lt.entity.Seckill;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;
import java.util.List;


/**
 * 配置spring和junit整合，junit启动时加载SpringIOC容器
 * spring-test，junit
 */
@RunWith(SpringJUnit4ClassRunner.class)
//告诉junit spring配置文件
@ContextConfiguration({"classpath:spring/spring-dao.xml"})
public class SeckillMapperTest {

    //注入Mapper
    @Autowired
    private SeckillMapper seckillMapper;

    @Test
    public void findAll() {
        List<Seckill> all = seckillMapper.findAll();

        for (Seckill seckill : all) {
            System.out.println(seckill.getTitle());
        }
    }

    @Test
    public void findById() {
        Seckill seckill = seckillMapper.findById(1l);
        System.out.println(seckill.getTitle());
    }

    @Test
    public void reduceStock() {
        int row = seckillMapper.reduceStock(1l, new Date());
        System.out.println(row);
    }
}