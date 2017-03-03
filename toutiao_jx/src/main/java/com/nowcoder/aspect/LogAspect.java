package com.nowcoder.aspect;

//import org.aspectj.lang.annotation.Aspect;
//import org.slf4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * Created by Jacinth on 2017/2/21.
 */
@Aspect
@Component
public class LogAspect {
    //本地异常日志记录对象
    private static final Logger logger = LoggerFactory.getLogger(LogAspect.class);

    @Before("execution(* com.nowcoder.controller.*Controller.*(..))")/*  *通配符，正则表达式，所有的返回值
                                                                      *   com.nowcoder.controller.*Controller类 */
    public void beforeMethod(JoinPoint joinPoint){
        StringBuilder sb=new StringBuilder();
        for(Object arg:joinPoint.getArgs()){
            sb.append("arg:"+arg.toString()+"|");
        }
        logger.info("before time: "+new Date());
        logger.info("before method: "+sb.toString());
    }

    @After("execution(* com.nowcoder.controller.IndexController.*(..))")
    public void afterMethod(JoinPoint joinPoint){
        logger.info("after time: "+new Date());
        logger.info("after method");
    }
}