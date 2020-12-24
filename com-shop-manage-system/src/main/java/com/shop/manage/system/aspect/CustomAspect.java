package com.shop.manage.system.aspect;

import com.alibaba.fastjson.JSON;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 切面编程，用于打印各controller 调用情况
 * @author Mr.joey
 */
@Aspect
@Component
public class CustomAspect {

    private static Logger logger = LoggerFactory.getLogger(CustomAspect.class);

    @Around("execution(* com.shop.manage.system.controller.*.*(..))")
    public Object handlerControllerBeforMethod(ProceedingJoinPoint point) throws Throwable {
        Object target = point.getSignature();
        logger.info("CustomAspect 监听,{}，方法入参列表==={}",target,JSON.toJSONString(point.getArgs()));

        Object proceed = point.proceed();

        logger.info("CustomAspect 监听，{}.方法响应===={}",target, JSON.toJSONString(proceed));

        return proceed;
    }

}
