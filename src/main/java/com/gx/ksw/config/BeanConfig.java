package com.gx.ksw.config;

import org.springframework.boot.ExitCodeGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author sgx
 * @version V1.0
 */
@Configuration
public class BeanConfig {
	@Bean
	public ExitCodeGenerator exitCodeGenerator() {
		return () -> 42;
	}
}
