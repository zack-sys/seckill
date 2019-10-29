package com.lzh.seckill.service.impl;

import com.fasterxml.jackson.core.format.DataFormatMatcher;
import com.lzh.seckill.dao.OrderDOMapper;
import com.lzh.seckill.dao.SequenceDOMapper;
import com.lzh.seckill.dataobject.OrderDO;
import com.lzh.seckill.dataobject.SequenceDO;
import com.lzh.seckill.error.BusinessException;
import com.lzh.seckill.error.CommonError;
import com.lzh.seckill.error.EmBusinessError;
import com.lzh.seckill.response.CommonReturnType;
import com.lzh.seckill.service.ItemService;
import com.lzh.seckill.service.OrderService;
import com.lzh.seckill.service.UserService;
import com.lzh.seckill.service.model.ItemModel;
import com.lzh.seckill.service.model.OrderModel;
import com.lzh.seckill.service.model.UserModel;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author li
 * 2019/10/27 19:44
 * version 1.0
 */
@Service
public class OrderServiceImpl implements OrderService {


    @Autowired
    private ItemService itemService;

    @Autowired
    private UserService userService;

    @Autowired
    private OrderDOMapper orderDOMapper;

    @Autowired
    private SequenceDOMapper sequenceDOMapper;



    @Override
    @Transactional
    public OrderModel createOrder(Integer userId, Integer itemId,Integer promoId, Integer amount) throws BusinessException {

        //1.效验下单状态 下单的商品是否存在 用户是否合法 购买数量是否正确
        ItemModel item = itemService.getItemById(itemId);
        if(item == null){
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"商品信息不存在");
        }
        //效验用户信息是否存在
        UserModel userById = userService.getUserById(userId);
        if(userById == null){
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"用户信息不存在");
        }

        if(amount<=0||amount>=99){
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"数量信息不符");
        }
        //校验活动信息
        if (promoId != null) {
            // 校验对应活动是否存在这个适用商品
            if (promoId.intValue() != item.getPromoModel().getId()) {
                throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"活动信息不正确");
                // 校验活动是否正在进行中
            } else if (item.getPromoModel().getStatus() != 2) {
                throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"活动尚未开始");
            }
        }


        //2.落单减库存
        boolean result = itemService.decreaseStock(itemId, amount);
        if(!result){
            throw new BusinessException(EmBusinessError.STOCK_NOT_ENOUGH);
        }

        //3.订单入库
        OrderModel orderModel = OrderModel.builder()
                .userId(userId)
                .itemId(itemId)
                .amount(amount)
                .itemPrice(item.getPrice())
                .promoId(promoId)
                .build();
        if(promoId!=null){
            orderModel.setPromoPrice(item.getPromoModel().getPromoItemPrice());
        }else{
            orderModel.setPromoPrice(item.getPrice());
        }
        orderModel.setOrderPrice(orderModel.getPromoPrice().multiply(new BigDecimal(amount)));


        //生成交易流水号
        orderModel.setId(generateOrderNo());

        //订单入库
        OrderDO orderDO = convertFromOrderModel(orderModel);
        orderDOMapper.insertSelective(orderDO);

        //商品销量增加
        itemService.increaseSales(itemId,amount);


        //4.返回前端

        return orderModel;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public String generateOrderNo() {
        //订单号 一共16位
        StringBuilder stringBuilder = new StringBuilder();
        // 前8位是  年月日
        LocalDateTime now = LocalDateTime.now();
        String newDate = now.format(DateTimeFormatter.ISO_DATE).replaceAll("-", "");
        stringBuilder.append(newDate);


        //中间6位是自增序列
        int sequence = 0;
        SequenceDO sequenceDO = sequenceDOMapper.getSequenceByName("order_info");
        sequence = sequenceDO.getCurrentValue();
        sequenceDO.setCurrentValue(sequenceDO.getCurrentValue()+sequenceDO.getStep());
        sequenceDOMapper.updateByPrimaryKeySelective(sequenceDO);

        //拼接
        String sequenceStr = String.valueOf(sequence);
        for (int i = 0; i < 6 - sequenceStr.length(); i++) {
            stringBuilder.append(0);
        }
        stringBuilder.append(sequenceStr);


        //最后两位是 分库分表位 暂无
        stringBuilder.append("00");


        return stringBuilder.toString();
    }

    private OrderDO convertFromOrderModel(OrderModel orderModel) {
        if (orderModel == null) {
            return null;
        }
        OrderDO orderDO = new OrderDO();
        BeanUtils.copyProperties(orderModel, orderDO);
        orderDO.setItemPrice(orderModel.getItemPrice().doubleValue());
        orderDO.setOrderPrice(orderModel.getOrderPrice().doubleValue());
        return orderDO;
    }

}
