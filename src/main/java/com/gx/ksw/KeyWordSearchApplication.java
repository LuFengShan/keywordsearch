package com.gx.ksw;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class KeyWordSearchApplication {

	public static void main(String[] args) {
		SpringApplication.run(KeyWordSearchApplication.class, args);
	}

}
