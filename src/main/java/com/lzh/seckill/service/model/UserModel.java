package com.lzh.seckill.service.model;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author li
 * 2019/10/26 15:55
 * version 1.0
 */
@Data
public class UserModel {

    @JsonView(SimpleUserModel.class)
    private Integer id;

    @JsonView(SimpleUserModel.class)
    @NotBlank(message = "用户名不能为空")
    private String name;

    @JsonView(SimpleUserModel.class)
    @NotNull(message = "性别不能填写")
    private Byte gender;

    @JsonView(SimpleUserModel.class)
    @Min(value = 0,message = "年龄必须大于")
    @Max(value = 150,message = "年龄必须小于150")
    private Integer age;

    @JsonView(SimpleUserModel.class)
    @NotBlank(message = "号码不能为空")
    private String telphone;

    @JsonView(SimpleUserModel.class)
    private String registerMode;

    @JsonView(ComplexUserModel.class)
    private Integer thirdPartyId;

    @JsonView(ComplexUserModel.class)
    @NotBlank(message = "密码不能为空")
    private String encrptPassowrd;

    public interface SimpleUserModel{}

    public interface ComplexUserModel extends SimpleUserModel{}
}
