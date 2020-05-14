package com.lt.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 秒杀订单表(秒杀订单表和其他订单表不同，属于独立的模块)
 */
@Data
public class SeckillOrder implements Serializable {

    private long seckillId; //秒杀到的商品ID
    private BigDecimal money; //支付金额

    private long userPhone; //秒杀用户的手机号

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime; //创建时间

    private boolean status; //订单状态， -1:无效 0:成功 1:已付款

    private Seckill seckill; //秒杀商品，和订单是一对多的关系

}
