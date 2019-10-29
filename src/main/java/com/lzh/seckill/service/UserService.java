package com.lzh.seckill.service;

import com.lzh.seckill.error.BusinessException;
import com.lzh.seckill.service.model.UserModel;

/**
 * @author li
 * 2019/10/26 15:55
 * version 1.0
 */
public interface UserService {
    /**
     * 通过用户ID获取用户对象的方法
     * @param id
     * @return
     */
    UserModel getUserById(Integer id);

    void register(UserModel userModel) throws BusinessException;
    UserModel validateLogin(String telphone,String encrptPassword) throws BusinessException;
}
