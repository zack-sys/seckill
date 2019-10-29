package com.lzh.seckill.controller;

import com.lzh.seckill.error.BusinessException;
import com.lzh.seckill.error.EmBusinessError;
import com.lzh.seckill.response.CommonReturnType;
import com.lzh.seckill.service.OrderService;
import com.lzh.seckill.service.model.OrderModel;
import com.lzh.seckill.service.model.UserModel;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * @author li
 * 2019/10/28 11:07
 * version 1.0
 */
@RestController
@RequestMapping("/order")
@Log4j2
@CrossOrigin(allowCredentials = "true",allowedHeaders = "*")  //跨域请求开启
public class OrderController extends BaseController{
    @Autowired
    private OrderService orderService;
    @Autowired
    private HttpServletRequest httpServletRequest;

    //封装下单请求
    @PostMapping(value = "/createorder",  consumes = {CONTENT_TYPE_FORMED})
    public CommonReturnType createOrder(@RequestParam(name = "itemId") Integer itemId,
                                        @RequestParam(name = "promoId",required = false) Integer promoId,
                                        @RequestParam(name = "amount") Integer amount) throws BusinessException {

        //获取用户登录信息
        Boolean isLogin = (Boolean) httpServletRequest.getSession().getAttribute("IS_LOGIN");
        if (isLogin == null || !isLogin.booleanValue()) {
            throw new BusinessException(EmBusinessError.USER_NOT_LOGIN, "用户还未登录，不能下单");
        }
        UserModel userModel = (UserModel) httpServletRequest.getSession().getAttribute("LOGIN_USER");


        OrderModel orderModel = orderService.createOrder(userModel.getId(), itemId,promoId, amount);

        return CommonReturnType.create(null);
    }

}
