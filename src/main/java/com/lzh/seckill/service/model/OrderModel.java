package com.lzh.seckill.service.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @author li
 * 2019/10/27 19:33
 * version 1.0
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderModel {

    //订单号
    private String id;

    //用户id
    private Integer userId;

    //商品id
    private Integer itemId;


    //秒杀商品id
    private Integer promoId;
    //购买商品的单价 若promoId 非空 则表示秒杀商品价格
    private BigDecimal promoPrice;

    //购买时候的价格
    private BigDecimal itemPrice;

    //购买数量
    private Integer amount;

    //购买金额
    private BigDecimal orderPrice;

}
