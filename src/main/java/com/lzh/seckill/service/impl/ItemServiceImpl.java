package com.lzh.seckill.service.impl;

import com.lzh.seckill.dao.ItemDOMapper;
import com.lzh.seckill.dao.ItemStockDOMapper;
import com.lzh.seckill.dataobject.ItemDO;
import com.lzh.seckill.dataobject.ItemStockDO;
import com.lzh.seckill.error.BusinessException;
import com.lzh.seckill.error.EmBusinessError;
import com.lzh.seckill.service.ItemService;
import com.lzh.seckill.service.PromoService;
import com.lzh.seckill.service.model.ItemModel;
import com.lzh.seckill.service.model.PromoModel;
import com.lzh.seckill.validator.ValidationResult;
import com.lzh.seckill.validator.ValidatorImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author li
 * 2019/10/27 11:10
 * version 1.0
 */
@Service
public class ItemServiceImpl implements ItemService {

    @Autowired
    private ValidatorImpl validator;

    @Autowired
    private ItemDOMapper itemDOMapper;

    @Autowired
    private ItemStockDOMapper itemStockDOMapper;

    @Autowired
    private PromoService promoService;

    @Override
    @Transactional
    public ItemModel createItem(ItemModel itemModel) throws BusinessException {
        //效验入参
        ValidationResult validate = validator.validate(itemModel);
        if(validate.isHasErrors()){
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,validate.getErrMsg());
        }

        //转化
        ItemDO itemDO = convertItemDoFromItemModel(itemModel);



        //写入数据库
        itemDOMapper.insertSelective(itemDO);

        itemModel.setId(itemDO.getId());

        //返回创建完成的对象
        ItemStockDO itemStockDO = convertItemModelFromItemDo(itemModel);

        itemStockDOMapper.insertSelective(itemStockDO);
        return getItemById(itemDO.getId());
    }

    @Override
    public List<ItemModel> listItem() {
        List<ItemDO> itemDOS = itemDOMapper.listItem();
        List<ItemModel> collect = itemDOS.stream().map(itemdo -> {
            ItemStockDO itemStockDO = itemStockDOMapper.selectByItemId(itemdo.getId());
            ItemModel itemModel = convertModelFromDataObject(itemdo, itemStockDO);
            return itemModel;
        }).collect(Collectors.toList());


        return collect;
    }

    @Override
    @Transactional
    public ItemModel getItemById(Integer id) {
        ItemDO itemDO = itemDOMapper.selectByPrimaryKey(id);
        if(itemDO == null){
            return null;
        }
        ItemStockDO itemStockDO = itemStockDOMapper.selectByItemId(id);

        ItemModel itemModel = convertModelFromDataObject(itemDO, itemStockDO);


        //获取活动商品信息
        PromoModel promoByItemId = promoService.getPromoByItemId(itemModel.getId());
        if(promoByItemId!=null&&promoByItemId.getStatus()!=3){
            itemModel.setPromoModel(promoByItemId);
        }

        return itemModel;
    }

    @Override
    @Transactional
    public boolean decreaseStock(Integer itemId, Integer amount) {
        //减库存
        int i = itemStockDOMapper.decreaseStock(itemId, amount);
        return i>0?true:false;
    }

    @Override
    @Transactional
    public void increaseSales(Integer itemId, Integer amount) {
        itemDOMapper.increaseSales(itemId,amount);

    }


    public ItemModel convertModelFromDataObject( ItemDO itemDO,ItemStockDO itemStockDO){
        if(itemDO == null){
            return null;
        }

        ItemModel itemModel = new ItemModel();

        BeanUtils.copyProperties(itemDO, itemModel);

        itemModel.setStock(itemStockDO.getStock());
        itemModel.setPrice(new BigDecimal(itemDO.getPrice()));
        return itemModel;
    }

    public ItemDO convertItemDoFromItemModel(ItemModel itemModel){
        if(itemModel == null){
            return null;
        }

        ItemDO itemDO = new ItemDO();
        BeanUtils.copyProperties(itemModel, itemDO);

        itemDO.setPrice(itemModel.getPrice().doubleValue());
        return itemDO;
    }


    public ItemStockDO convertItemModelFromItemDo(ItemModel itemModel){
        if(itemModel == null){
            return null;
        }

        ItemStockDO itemStockDO = new ItemStockDO();
        itemStockDO.setItemId(itemModel.getId());
        itemStockDO.setStock(itemModel.getStock());

        return itemStockDO;
    }
}
