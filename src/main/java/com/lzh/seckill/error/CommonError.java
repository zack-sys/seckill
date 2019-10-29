package com.lzh.seckill.error;

/**
 * @author li
 * 2019/10/26 16:46
 * version 1.0
 */
public interface CommonError {
    public int getErrCode();

    public String getErrMsg();

    public CommonError setErrMsg(String errMSg);
}
