package com.lzh.seckill.controller;

import com.alibaba.druid.util.StringUtils;
import com.fasterxml.jackson.annotation.JsonView;
import com.lzh.seckill.dao.UserDOMapper;
import com.lzh.seckill.dataobject.UserDO;
import com.lzh.seckill.error.BusinessException;
import com.lzh.seckill.error.CommonError;
import com.lzh.seckill.error.EmBusinessError;
import com.lzh.seckill.response.CommonReturnType;
import com.lzh.seckill.service.UserService;
import com.lzh.seckill.service.model.UserModel;

import com.sun.org.apache.regexp.internal.RE;
import lombok.extern.log4j.Log4j;
import lombok.extern.log4j.Log4j2;
import org.apache.tomcat.util.security.MD5Encoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import sun.misc.BASE64Encoder;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;


/**
 * @author li
 * 2019/10/26 15:26
 * version 1.0
 */
@RestController
@Log4j2
@RequestMapping("/user")
@CrossOrigin(allowCredentials = "true",allowedHeaders = "*")  //跨域请求开启
// 允许跨域传输所有的header参数 讲用于使用token放入header域 做session共享的跨域请求
// 需要配合前端
public class UserController extends BaseController{

    @Autowired
    private UserDOMapper userDOMapper;

    @Autowired
    private UserService userService;

    @Autowired
    private HttpServletRequest request;

    @RequestMapping("/")
    public String home() throws BusinessException {
        UserDO userDO = userDOMapper.selectByPrimaryKey(1);
        if(userDO==null){
            throw new BusinessException(EmBusinessError.USER_NOT_EXIST);
        }else{
            return userDO.getName();
        }
    }

    @PostMapping(value = "/login")
    public CommonReturnType login(String telphone, String password) throws BusinessException, UnsupportedEncodingException, NoSuchAlgorithmException {
        if(telphone==null||telphone.equals("")||password == null || password.equals("")){
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"用户名和密码不能为空");
        }
        //验证用户登录是否合法
        UserModel userModel = userService.validateLogin(telphone, EncodeByMd5(password));

        //将登陆凭证加入到用户登录成功的session

        this.request.getSession().setAttribute("IS_LOGIN", true);
        this.request.getSession().setAttribute("LOGIN_USER", userModel);


        return CommonReturnType.success(null);

    }

    @PostMapping(value = "/register",consumes = {CONTENT_TYPE_FORMED})
    public CommonReturnType register(@RequestParam("name") String name,
                                     @RequestParam("gender")Integer gender,
                                     @RequestParam("age")Integer age,
                                     @RequestParam("telphone")String telphone,
                                     @RequestParam("code")String code,
                                     @RequestParam("password")String password) throws BusinessException, UnsupportedEncodingException, NoSuchAlgorithmException {


        String isSessiontelphone = (String) request.getSession().getAttribute(telphone);
        if(isSessiontelphone==null){
            throw new BusinessException(EmBusinessError.CODE_NOT_FOUNT);
        }
        if(!StringUtils.equals(isSessiontelphone,code)){
            throw new BusinessException(EmBusinessError.CODE_ERROR);
        }
        //注册
        UserModel userModel = new UserModel();
        userModel.setName(name);
        userModel.setAge(age);
        userModel.setGender(new Byte(String.valueOf(gender.intValue())));
        userModel.setTelphone(telphone);
        userModel.setEncrptPassowrd(EncodeByMd5(password));
        userModel.setRegisterMode("byphone");

        log.info("userModel: "+ userModel);
        userService.register(userModel);
        return CommonReturnType.create(null);
    }
    public String EncodeByMd5(String str) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        BASE64Encoder base64Encoder = new BASE64Encoder();
        String encode = base64Encoder.encode(md5.digest(str.getBytes("utf-8")));
        return encode;
    }


    @PostMapping(value = "/getotp",consumes = {CONTENT_TYPE_FORMED})
    public CommonReturnType getOtp(@RequestParam("telphone") String telphone)  {

        //生成短信验证码

        Random random = new Random();
        int i = random.nextInt(89999);
        i+=10000;
        String code = String.valueOf(i);

        //吧短信验证码保存起来
        request.getSession().setAttribute(telphone, code);

        //发送短信

        log.info("手机号: "+telphone+",的验证码为:"+code);
        return CommonReturnType.success(code);
    }


    @RequestMapping("/get")
    //@JsonView(UserModel.SimpleUserModel.class)
    public CommonReturnType getUser(@RequestParam("id") Integer id) throws BusinessException {
        UserModel userModel = userService.getUserById(id);

        if(userModel==null){
            throw new BusinessException(EmBusinessError.USER_NOT_EXIST);
        }

        return CommonReturnType.create(userModel);
    }


}
