package com.lzh.seckill.service;

import com.lzh.seckill.service.model.PromoModel;

/**
 * @author li
 * 2019/10/28 19:02
 * version 1.0
 */
public interface PromoService {

    //获取当前商品 将要进行的或者正在进行的秒杀活动
    PromoModel getPromoByItemId(Integer itemId);
}
