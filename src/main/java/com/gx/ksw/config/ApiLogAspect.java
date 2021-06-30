package com.gx.ksw.config;

import com.alibaba.fastjson.JSON;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @Author qinglang
 **/

@Aspect
@Component
public class ApiLogAspect {


    @Pointcut("execution(* com.gx.ksw.controller.*.*(..))")
    public void methodPointCut() {
    }

    @Around("methodPointCut()")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {
        Signature signature = pjp.getSignature();
        Long startTime = null;
        Long endTime = null;
        //被代理的类的类名
        String className = pjp.getTarget().getClass().getName();
        //方法名
        String methodName = signature.getName();
        //获取日志
        Logger logger = LoggerFactory.getLogger(className);
        //参数数组
        Object[] requestParams = pjp.getArgs();
        StringBuffer sb = new StringBuffer();
        for (Object requestParam : requestParams) {
            if (requestParam != null) {
                sb.append(JSON.toJSONString(requestParam));
                sb.append(",");
            }
        }
        String requestParamsString = sb.toString();
        if (requestParamsString.length() > 0) {
            requestParamsString = requestParamsString.substring(0, requestParamsString.length() - 1);
        }
        //接口应答前打印日志
//        logger.info(String.format("【%s】类的【%s】方法，请求参数：%s", className, methodName, requestParamsString));
        //接口调用开始响应起始时间
        startTime = System.currentTimeMillis();
        //正常执行方法
        Object response = pjp.proceed();
        //接口调用结束时间
        endTime = System.currentTimeMillis();
        //接口应答之后打印日志
//        logger.info(String.format("【%s】类的【%s】方法，应答参数：%s", className, methodName, JSON.toJSONString(response)));

        //接口耗时
        logger.info(String.format("接口切面拦截信息,【%s】总耗时(毫秒):%s", className + "." + methodName, (endTime - startTime)));

        //异常处理

        return response;

    }
}