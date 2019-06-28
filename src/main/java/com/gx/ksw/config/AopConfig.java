package com.gx.ksw.config;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.springframework.stereotype.Component;

import java.time.Instant;

/**
 * AOP配置
 *
 * @author sgx
 * @version V1.1.0
 * @date 2019/6/28 16:08
 * @since V1.1.0
 */
@Slf4j
@Component
public class AopConfig {

	@AfterReturning("execution(* com.gx.ksw.*.*(..))")
	public void logServiceAccess(JoinPoint joinPoint) {
		log.error(joinPoint + " -> " + Instant.now().getEpochSecond());
	}
}
