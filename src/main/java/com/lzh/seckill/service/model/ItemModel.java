package com.lzh.seckill.service.model;



import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;
import java.math.BigDecimal;

/**
 * @author li
 * 2019/10/27 10:55
 * version 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemModel {

    //商品ID
    private Integer id;

    //商品名称
    @NotBlank(message = "商品名称不能为空")
    private String title;

    //商品价格
    @NotNull(message = "商品价格不能为空")
    @Min(value = 0,message = "商品价格必须大于0")
    private BigDecimal price;

    //商品库存
    @NotNull(message = "商品库存不能不填")
    private Integer stock;

    //商品描述
    @NotBlank(message = "商品描述不能为空")
    private String description;

    //商品销量
    private Integer sales;

    //商品图标
    @NotBlank(message = "图片信息不能为空")
    private String imgUrl;

    //如果此值不为空 那么就说明存在还未结束的秒杀活动
    private PromoModel promoModel;

}
