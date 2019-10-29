package com.lzh.seckill.error;

/**
 * @author li
 * 2019/10/26 16:57
 * version 1.0
 */
//包装器业务类型实现
public class BusinessException extends Exception implements CommonError {

    private CommonError commonError;


    public BusinessException(CommonError commonError) {
        super();
        this.commonError = commonError;
    }

    public BusinessException(CommonError commonError, String errMsg) {
        super();
        this.commonError = commonError;
        this.commonError.setErrMsg(errMsg);
    }

    @Override
    public int getErrCode() {
        return commonError.getErrCode();
    }

    @Override
    public String getErrMsg() {
        return commonError.getErrMsg();
    }

    @Override
    public CommonError setErrMsg(String errMSg) {
        commonError.setErrMsg(errMSg);
        return commonError;
    }
}
