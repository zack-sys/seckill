package com.lzh.seckill.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author li
 * 2019/10/26 16:26
 * version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommonReturnType {

    // -1 失败  1 成功
    private Integer status;

    // success or  fail
  //  private String status;
    private Object data;

    /**
     * 默认成功
     * @param data
     * @return
     */
    public static CommonReturnType create(Object data){
        CommonReturnType commonReturnType = new CommonReturnType();
        commonReturnType.setStatus(1);
        commonReturnType.setData(data);
        return commonReturnType;
    }
    public static CommonReturnType success(Object data){
        CommonReturnType commonReturnType = new CommonReturnType();
        commonReturnType.setStatus(1);
        commonReturnType.setData(data);
        return commonReturnType;
    }

    public static CommonReturnType fail(Object data){
        CommonReturnType commonReturnType = new CommonReturnType();
        commonReturnType.setStatus(-1);
        commonReturnType.setData(data);
        return commonReturnType;
    }



    /**
     * 返回
     * @param code
     * @param data
     * @return
     */
    public static CommonReturnType create(Integer code,Object data){
        CommonReturnType commonReturnType = new CommonReturnType();
        commonReturnType.setStatus(code);
        commonReturnType.setData(data);
        return commonReturnType;
    }

}
