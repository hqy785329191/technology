package com.shop.manage.system.config;

import com.shop.manage.system.interceptor.CustomInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 自定义拦截器配置
 * @author Mr.joey
 */
@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

    @Autowired
    CustomInterceptor customInterceptor;

    /**
     * 手动注册拦截器到过滤器链
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(customInterceptor);
    }
}
