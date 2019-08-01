package com.gx.ksw.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Configuration;

import java.lang.reflect.Method;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * 自定义缓存的配置
 * @author sgx
 * @version V1.1.0
 * @date 2019/6/28 15:31
 * @since V1.1.0
 */
@Slf4j
@Configuration
public class RedisConfig extends CachingConfigurerSupport {

	/**
	 * 自定义缓存的Key
	 * @return
	 */
	@Override
	public KeyGenerator keyGenerator() {
		return (target, method, params) -> {
			StringBuilder sb = new StringBuilder();
			String className = Pattern.compile("\\.")
					.splitAsStream(target.getClass().getName()) // 缓存类的全路径
					.map(s -> String.valueOf(s.charAt(0))) // 取各个路径的第一个字母
					.collect(Collectors.joining(".")); // 用点拼接起来
			sb.append(className);
			sb.append(".");
			sb.append(method.getName());
			if (params.length > 0) {
				for (Object obj : params) {
					sb.append("-");
					sb.append(obj.toString());
				}
			}
			log.info("缓存中的key:" + sb.toString());
			return sb.toString();
		};
	}
}
