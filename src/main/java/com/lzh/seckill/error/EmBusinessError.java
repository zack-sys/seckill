package com.lzh.seckill.error;

import com.lzh.seckill.response.CommonReturnType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author li
 * 2019/10/26 16:48
 * version 1.0
 */


public enum EmBusinessError implements CommonError {

    /**
     * 通用错误类型00001  参数不合法
     */
    //  通用错误类型 1000 开头
    PARAMETER_VALIDATION_ERROR(10001, "参数不合法"),
    UNKNOW_ERROR(10002, "未知错误"),
    CODE_ERROR(10003, "验证码错误"),
    CODE_NOT_FOUNT(10003, "验证码不存在"),
    USER_NOT_FOUNT(10004, "用户手机号或密码不正确"),

    // 20000 开头 用户信息相关错误定义
    //用户名不存在
    USER_NOT_EXIST(20001, "用户不存在"),
    USER_NOT_LOGIN(20003,"用户未登录"),

    //30000开头 为交易信息错误定义
    STOCK_NOT_ENOUGH(30001,"库存不足"),

    ;


    private int errCode;
    private String errMsg;

    private EmBusinessError(int errCode, String errMsg) {
        this.errCode = errCode;
        this.errMsg = errMsg;
    }

    @Override
    public int getErrCode() {
        return errCode;
    }

    @Override
    public String getErrMsg() {
        return errMsg;
    }

    @Override
    public CommonError setErrMsg(String errMSg) {
        this.errMsg = errMSg;
        return this;
    }


}
