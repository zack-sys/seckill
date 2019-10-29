package com.lzh.seckill.service;

import com.lzh.seckill.error.BusinessException;
import com.lzh.seckill.service.model.ItemModel;

import java.util.List;

/**
 * @author li
 * 2019/10/27 11:09
 * version 1.0
 */
public interface ItemService {

    //创建商品

    ItemModel createItem(ItemModel itemModel) throws BusinessException;

    //商品列表浏览
    List<ItemModel> listItem();

    //商品详情浏览
    ItemModel getItemById(Integer id);


    //库存扣减
    boolean decreaseStock(Integer itemId,Integer amount);

    //商品销量增加
    void increaseSales(Integer itemId,Integer amount);

}
