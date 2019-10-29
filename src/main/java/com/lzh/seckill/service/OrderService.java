package com.lzh.seckill.service;

import com.lzh.seckill.error.BusinessException;
import com.lzh.seckill.service.model.OrderModel;

/**
 * @author li
 * 2019/10/27 19:40
 * version 1.0
 */
public interface OrderService {

    OrderModel createOrder(Integer userId,Integer itemId,Integer promoId,Integer amount) throws BusinessException;
}
