package com.lzh.seckill.controller;

import com.lzh.seckill.error.BusinessException;
import com.lzh.seckill.error.EmBusinessError;
import com.lzh.seckill.response.CommonReturnType;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * @author li
 * 2019/10/26 18:52
 * version 1.0
 */
@ControllerAdvice
public class BaseException {
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Object exception(HttpServletRequest request, Exception ex) {
        ex.printStackTrace();
        Map<String,Object> map = new HashMap<>();
        if(ex instanceof BusinessException){
            BusinessException businessException = (BusinessException) ex;
            map.put("code", businessException.getErrCode());
            map.put("errMsg", businessException.getErrMsg());
        }else{
            map.put("code", EmBusinessError.UNKNOW_ERROR.getErrCode());
            map.put("errMsg", EmBusinessError.UNKNOW_ERROR.getErrMsg());
        }


        return CommonReturnType.fail(map);
    }

}
