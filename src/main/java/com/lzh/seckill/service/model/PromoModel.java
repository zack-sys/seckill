package com.lzh.seckill.service.model;

import lombok.Data;
import org.joda.time.DateTime;

import java.math.BigDecimal;

/**
 * @author li
 * 2019/10/28 13:19
 * version 1.0
 */
@Data
public class PromoModel {


    private Integer id;


    //秒杀活动名称
    private String promoName;

    //秒杀活动状态 1 未开始 2 正在进行中 3 已结束
    private Integer status;


    //秒杀活动的开始时间
    private DateTime startDate;

    private DateTime endDate;

    //秒杀活动的适用商品
    private Integer itemId;


    //秒杀活动的商品价格
    private BigDecimal promoItemPrice;











}
