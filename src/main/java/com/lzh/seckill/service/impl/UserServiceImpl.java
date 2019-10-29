package com.lzh.seckill.service.impl;

import com.lzh.seckill.dao.UserDOMapper;
import com.lzh.seckill.dao.UserPasswordDOMapper;
import com.lzh.seckill.dataobject.UserDO;
import com.lzh.seckill.dataobject.UserPasswordDO;
import com.lzh.seckill.error.BusinessException;
import com.lzh.seckill.error.EmBusinessError;
import com.lzh.seckill.service.UserService;
import com.lzh.seckill.service.model.UserModel;
import com.lzh.seckill.validator.ValidationResult;
import com.lzh.seckill.validator.ValidatorImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sun.misc.BASE64Encoder;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author li
 * 2019/10/26 15:55
 * version 1.0
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDOMapper userDOMapper;

    @Autowired
    private UserPasswordDOMapper userPasswordDOMapper;


    @Autowired
    private ValidatorImpl validator;

    @Override
    public UserModel getUserById(Integer id) {
        UserDO userDO = userDOMapper.selectByPrimaryKey(id);
        if(userDO==null){
            return null;
        }
        UserPasswordDO userPasswordDO = userPasswordDOMapper.selectByUserId(userDO.getId());

        return convertFromDataObject(userDO,userPasswordDO);
    }

    @Override
    @Transactional
    public void register(UserModel userModel) throws BusinessException {
        if(userModel == null){
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR);
        }

//        if(StringUtils.isEmpty(userModel.getName())
//                ||userModel.getGender() == null
//                ||userModel.getAge() == null
//                ||StringUtils.isEmpty(userModel.getTelphone())){
//            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR);
//        }

        ValidationResult validate = validator.validate(userModel);
        if(validate.isHasErrors()){
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,validate.getErrMsg());
        }


        // userModel - > UserDo
        UserDO userDO = convertFromDataModel(userModel);
        try {
            userDOMapper.insertSelective(userDO);
        }catch (DuplicateKeyException e){
            throw new BusinessException(EmBusinessError.UNKNOW_ERROR,"手机号码已存在");
        }


        userModel.setId(userDO.getId());

        UserPasswordDO userPasswordDO = convertPasswordFromModel(userModel);
        userPasswordDOMapper.insertSelective(userPasswordDO);

        return;

    }

    @Override
    public UserModel validateLogin(String telphone, String encrptPassword) throws BusinessException {
        UserDO userDO = userDOMapper.selectByTelphone(telphone);

        if(userDO == null){
            throw new BusinessException(EmBusinessError.USER_NOT_FOUNT);
        }

        UserPasswordDO userPasswordDO = userPasswordDOMapper.selectByUserId(userDO.getId());
        UserModel userModel = convertFromDataObject(userDO, userPasswordDO);

        //判断密码
        if(!StringUtils.equals(userModel.getEncrptPassowrd(),encrptPassword)){
            throw new BusinessException(EmBusinessError.USER_NOT_FOUNT);
        }
        return userModel;

    }


    public UserPasswordDO convertPasswordFromModel(UserModel userModel){
        if(userModel == null){
            return null;
        }
        UserPasswordDO userPasswordDO = new UserPasswordDO();
        userPasswordDO.setEncrptPassword(userModel.getEncrptPassowrd());
        userPasswordDO.setUserId(userModel.getId());
        return userPasswordDO;
    }
    public UserDO convertFromDataModel(UserModel userModel){
        if(userModel == null){
            return null;
        }

        UserDO userDO = new UserDO();
        BeanUtils.copyProperties(userModel, userDO);
        return userDO;
    }


    public UserModel convertFromDataObject(UserDO userDO, UserPasswordDO userPasswordDO){
        if(userDO==null){
            return null;
        }

        UserModel userModel = new UserModel();
        BeanUtils.copyProperties(userDO,userModel);

        if(userPasswordDO!=null){
            userModel.setEncrptPassowrd(userPasswordDO.getEncrptPassword());
        }

        return userModel;
    }
}
