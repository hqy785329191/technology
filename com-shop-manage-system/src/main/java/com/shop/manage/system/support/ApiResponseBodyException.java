package com.shop.manage.system.support;

import com.shop.manage.system.exception.CustomException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

/**
 * controller 异常响应统一返回值
 * @author Mr.joey
 */
@RestControllerAdvice(basePackages = "com.shop.manage.system.controller")
public class ApiResponseBodyException {

    @ExceptionHandler(CustomException.class)
    @ResponseBody
    public Map<String,Object> CustomExceptionHandler(CustomException ex){
        Map<String,Object> map = new HashMap<>(16);
        map.put("code",ex.getCode());
        map.put("msg",ex.getMsg());
        return map;
    }
}
