package com.lzh.seckill.validator;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Set;

/**
 * @author li
 * 2019/10/27 10:33
 * version 1.0
 */
@Component
public class ValidatorImpl implements InitializingBean {

    private Validator validator;

    //实现效验方法并返回效验结果
    public ValidationResult validate(Object bean){
        ValidationResult result= new ValidationResult();
        Set<ConstraintViolation<Object>> constraintViolations = validator.validate(bean);

        if(constraintViolations.size()>0){
            //有错误
            result.setHasErrors(true);
            constraintViolations.forEach( constraintViolation ->{
                String errMsg = constraintViolation.getMessage();
                String propertyName = constraintViolation.getPropertyPath().toString();
                result.getErrorMsgMap().put(propertyName,errMsg);
            });
        }

        return result;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        validator = Validation.buildDefaultValidatorFactory().getValidator();

    }
}
