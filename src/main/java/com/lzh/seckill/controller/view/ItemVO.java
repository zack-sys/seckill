package com.lzh.seckill.controller.view;


import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.joda.time.DateTime;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * @author li
 * 2019/10/27 10:55
 * version 1.0
 */
@Data
@NoArgsConstructor
public class ItemVO {

    //商品ID
    private Integer id;

    //商品名称
    private String title;

    //商品价格
    private BigDecimal price;

    // 秒杀活动价格
    private BigDecimal promoPrice;

    //秒杀活动ID
    private Integer promoId;

    //秒杀活动开始时间
    private String startDate;

    //商品库存
    private Integer stock;

    //商品描述
    private String description;

    //商品销量
    private Integer sales;

    //商品图标
    private String imgUrl;

    /**
     * 记录商品是否在秒杀中
     * 0 表示没有秒杀活动
     * 1 表示秒杀活动待开始
     * 2 表示秒杀活动进行中
     */
    private Integer promoStatus;


}
