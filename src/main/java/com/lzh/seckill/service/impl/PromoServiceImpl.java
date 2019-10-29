package com.lzh.seckill.service.impl;

import com.lzh.seckill.dao.PromoDOMapper;
import com.lzh.seckill.dataobject.PromoDO;
import com.lzh.seckill.error.BusinessException;
import com.lzh.seckill.service.PromoService;
import com.lzh.seckill.service.model.PromoModel;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * @author li
 * 2019/10/28 19:04
 * version 1.0
 */
@Service
public class PromoServiceImpl implements PromoService {

    @Autowired
    private PromoDOMapper promoDOMapper;

    @Override
    public PromoModel getPromoByItemId(Integer itemId) {
        PromoDO promoDO = promoDOMapper.selectByItemId(itemId);

        //databoject -> model
        PromoModel promoModel = convertFromDataObject(promoDO);
        if(promoModel == null){
            return null;
        }

        //判断当前时间是否秒杀活动即将开始进行
        DateTime now = new DateTime();
        //如果秒杀时间在 现在时间后面
        if(promoModel.getStartDate().isAfterNow()){
            promoModel.setStatus(1);
        }else if(promoModel.getEndDate().isBeforeNow()){ // 如果秒杀结束时间在当前时间 前面 那么 已结束
            promoModel.setStatus(3);
        }else{
            promoModel.setStatus(2);
        }


        return promoModel;
    }

    private PromoModel convertFromDataObject(PromoDO promoDO) {
        if (promoDO == null) {
            return null;
        }
        PromoModel promoModel = new PromoModel();
        BeanUtils.copyProperties(promoDO, promoModel);
        promoModel.setStartDate(new DateTime(promoDO.getStartDate()));
        promoModel.setEndDate(new DateTime(promoDO.getEndDate()));
        promoModel.setPromoItemPrice(new BigDecimal(promoDO.getPromoItemPrice().doubleValue()));

        return promoModel;
    }

}
