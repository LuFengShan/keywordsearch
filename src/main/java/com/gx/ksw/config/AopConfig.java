package com.gx.ksw.config;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.context.annotation.Description;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Arrays;

/**
 * AOP配置
 *
 * @author sgx
 * @version V1.1.0
 * @date 2019/6/28 16:08
 * @since V1.1.0
 */
@Slf4j
//@Aspect
//@Component
@Description("AOP切面")
public class AopConfig {
	private long start;
	private long end;

	@Pointcut("execution(* com.gx.ksw.controller.*.*(..))")
	public void testCut() {

	}

	@Before("testCut()")
	public void cutProcess(JoinPoint joinPoint) {
		MethodSignature signature = (MethodSignature) joinPoint.getSignature();
		Method method = signature.getMethod();
		log.info("Before -> 注解方式AOP开始拦截, 当前拦截的方法名: " + method.getName());
	}

	@After("testCut()")
	public void after(JoinPoint joinPoint) {
		MethodSignature signature = (MethodSignature) joinPoint.getSignature();
		Method method = signature.getMethod();
		log.info("After -> 注解方式AOP执行的方法 :" + method.getName() + " 执行完了");
	}


	@Around("testCut()")
	public Object testCutAround(ProceedingJoinPoint joinPoint) throws Throwable {
		start = Instant.now().getEpochSecond();
		log.info("Around -> 注解方式AOP拦截开始进入环绕通知.......");
		Object proceed = joinPoint.proceed();
		end = Instant.now().getEpochSecond();
		log.info("Around -> 准备退出环绕......");
		log.info((end - start) + "s");
		return proceed;
	}

	/**
	 * returning属性指定连接点方法返回的结果放置在result变量中
	 *
	 * @param joinPoint 连接点
	 * @param result    返回结果
	 */
	@AfterReturning(value = "testCut()", returning = "result")
	public void afterReturn(JoinPoint joinPoint, Object result) {
		MethodSignature signature = (MethodSignature) joinPoint.getSignature();
		Method method = signature.getMethod();
		log.info("AfterReturning -> 注解方式AOP拦截的方法执行成功, 进入返回通知拦截, 方法名为: " + method.getName() + ", 返回结果为: " + result.toString());
	}

	@AfterThrowing(value = "testCut()", throwing = "e")
	public void afterThrow(JoinPoint joinPoint, Exception e) {
		MethodSignature signature = (MethodSignature) joinPoint.getSignature();
		Method method = signature.getMethod();
		log.info("AfterThrowing -> 注解方式AOP进入方法异常拦截, 方法名为: " + method.getName() + ", 异常信息为: " + e.getMessage());
	}
}
