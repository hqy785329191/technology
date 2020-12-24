package com.shop.manage.system.support;

import com.alibaba.fastjson.JSON;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * controller 统一返回值
 * @author Mr.joey
 */
@RestControllerAdvice(basePackages = "com.shop.manage.system.controller")
public class ApiResponseBody implements ResponseBodyAdvice<Object> {

    @Override
    public boolean supports(MethodParameter returnType, Class converterType) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType
            , Class selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        // 防止二次封装
        if (body instanceof Response) {
            return body;
        }

        // 处理controller返回为字符串时, 转换报异常的bug（默认使用的jackson转换器会报类型转换的错，感兴趣可以跟下源代码）
        //（如果使用FastJsonHttpMessageConverter，则不需要加下面if判断）
        if (body instanceof String) {
            return JSON.toJSONString(Response.ok(body));
        }

        return Response.ok(body);
    }
}
