package com.lzh.seckill.controller;

import com.lzh.seckill.controller.view.ItemVO;
import com.lzh.seckill.error.BusinessException;
import com.lzh.seckill.response.CommonReturnType;
import com.lzh.seckill.service.ItemService;
import com.lzh.seckill.service.model.ItemModel;
import org.joda.time.format.DateTimeFormat;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author li
 * 2019/10/27 12:58
 * version 1.0
 */
@RestController
@RequestMapping("/item")
@CrossOrigin(allowCredentials = "true",allowedHeaders = "*")  //跨域请求开启
public class ItemController extends BaseController{

    @Autowired
    private ItemService itemService;


    //创建商品的controller
    @PostMapping(value = "/create", consumes = {CONTENT_TYPE_FORMED})
    public CommonReturnType createItem(String title, String description, BigDecimal price,Integer stock,String imgUrl) throws BusinessException {
        //封装service 请求用在创建商品
        ItemModel itemModel = ItemModel.builder()
                .title(title)
                .description(description)
                .price(price)
                .stock(stock)
                .imgUrl(imgUrl).build();


        ItemModel item = itemService.createItem(itemModel);
        ItemVO itemVO = convertVOFromModel(item);

        return CommonReturnType.create(itemVO);
    }

    //商品详情页浏览
    @GetMapping(value = "/get")
    public CommonReturnType getItem(@RequestParam(name = "id") Integer id) {
        ItemModel itemModel = itemService.getItemById(id);

        ItemVO itemVO = convertVOFromModel(itemModel);

        return CommonReturnType.create(itemVO);
    }
    //商品列表页面浏览
    @GetMapping(value = "/list")
    public CommonReturnType listItem() {
        List<ItemModel> itemModelList = itemService.listItem();

        List<ItemVO> itemVOList = itemModelList.stream().map(itemModel -> {
            ItemVO itemVO = this.convertVOFromModel(itemModel);
            return itemVO;
        }).collect(Collectors.toList());

        return CommonReturnType.create(itemVOList);
    }


    public ItemVO convertVOFromModel(ItemModel itemModel){
        if(itemModel == null){
            return null;
        }

        ItemVO itemVO = new ItemVO();
        BeanUtils.copyProperties(itemModel, itemVO);
        if(itemModel.getPromoModel()!=null){
            itemVO.setPromoStatus(itemModel.getPromoModel().getStatus());
            itemVO.setPromoId(itemModel.getPromoModel().getId());
            itemVO.setStartDate(itemModel.getPromoModel().getStartDate().toString(DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")));
            itemVO.setPromoPrice(itemModel.getPromoModel().getPromoItemPrice());
        }else{
            itemVO.setPromoStatus(0);
        }
        return itemVO;
    }
}
