package com.shop.manage.system.aspect;

import com.alibaba.fastjson.JSON;
import com.shop.manage.system.annotation.AspectIgnoreLog;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;

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
        Signature signature = point.getSignature();
        Class clazz = signature.getDeclaringType();

        //如果类上面，或者方法上面，添加这个 AspectIgnoreLog 注解，则不进行参数打印
        Annotation annotation = clazz.getAnnotation(AspectIgnoreLog.class);
        if(annotation==null){
            logger.info("CustomAspect 监听,{}，方法入参列表==={}",point.getTarget(),JSON.toJSONString(point.getArgs()));
        }

        Object proceed = point.proceed();

        logger.info("CustomAspect 监听，{}.方法响应===={}",point.getTarget(), JSON.toJSONString(proceed));

        return proceed;
    }

}
