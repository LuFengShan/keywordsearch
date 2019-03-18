package com.gx.ksw.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Collections;

/**
 * Swagger2的配置文件
 * http://localhost:8989/1.1.0/swagger-ui.html
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {

	@Bean
	public Docket swaggerSpringMvcPlugin() {
		return new Docket(DocumentationType.SWAGGER_2)
				.apiInfo(apiInfo())
				.select() // 创建一个构建器，用于定义哪些控制器及其生成的文档中应包含哪些方法。
				.apis(RequestHandlerSelectors.basePackage("com.gx.ksw"))
				.paths(PathSelectors.any())
				.build();
	}

	private ApiInfo apiInfo() {
		return new ApiInfoBuilder()
				.title("关键字检索工具") // 此API的标题
				.description("提供关键字的检索") // 此API的描述
				.termsOfServiceUrl("http://127.0.0.1:8989/1.1.0/swagger-ui.html") // 更新此API服务条款网址
				.version("1.1.0") // 此API的版本
				.contact(new Contact("sgx", "http://127.0.0.1:8989/1.1.0/swagger-ui.html","sunguangxu@aliyun.com")) // 更新负责此API的人员的联系信息
				.license("MIT") // 更新此API的许可证信息
				.extensions(null) // 添加此API的扩展程序
				.build();
	}

}
