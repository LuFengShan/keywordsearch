package com.gx.ksw.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMethod;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.ResponseMessageBuilder;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.ResponseMessage;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

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
				// apis()：这种方式我们可以通过指定包名的方式，让 Swagger 只去某些包下面扫描
				.apis(RequestHandlerSelectors.basePackage("com.gx.ksw"))
				// paths()：这种方式可以通过筛选 API 的 url 来进行过滤
				.paths(PathSelectors.any())
				.build()
				.useDefaultResponseMessages(false)
				.globalResponseMessage(RequestMethod.GET, restGlobalResponseMessage());
	}

	/**
	 * 文档信息配置，文档的版本号、联系人邮箱、网站、版权、开源协议等等信息，
	 * 但与上面几条不同的是这些信息不是通过注解配置，而是通过创建一个 ApiInfo 对象，
	 * 并且使用 Docket.appInfo() 方法来设置
	 * @return
	 */
	private ApiInfo apiInfo() {
		return new ApiInfoBuilder()
				.title("关键字检索工具") // 此API的标题
				.description("提供关键字的检索") // 此API的描述
				.termsOfServiceUrl("http://127.0.0.1:8989/1.1.0/swagger-ui.html") // 更新此API服务条款网址
				.version("1.1.0") // 此API的版本
				.contact(new Contact("sgx", "http://127.0.0.1:8989/1.1.0/swagger-ui.html","SGX@aliyun.com")) // 更新负责此API的人员的联系信息
				.license("XXXX") // 更新此API的许可证信息
				.extensions(null) // 添加此API的扩展程序
				.build();
	}


	/**
	 * 自定义响应消息
	 * Swagger 允许我们通过 Docket 的 globalResponseMessage() 方法全局覆盖 HTTP 方法的响应消息，
	 * 但是首先我们得通过 Docket 的 useDefaultResponseMessages 方法告诉 Swagger 不使用默认的 HTTP 响应消息，
	 * 假设我们现在需要覆盖所有 GET 方法的 500 和 403 错误的响应消息，
	 * 我们只需要在 SwaggerConfig.java 类中的 Docket Bean 下添加如下内容：
	 * @return
	 */
	private List<ResponseMessage> restGlobalResponseMessage() {
		Supplier<ResponseMessageBuilder> supplier = ResponseMessageBuilder::new;
		List<ResponseMessage> list = new ArrayList<>();
		ResponseMessage build = supplier.get()
				.code(500)
				.message("服务器发生异常")
				.responseModel(new ModelRef("Error"))
				.build();
		list.add(build);
		build = supplier.get()
				.code(403)
				.message("资源不可用")
				.build();
		list.add(build);
		build = supplier.get()
				.code(200)
				.message("请求成功")
				.build();
		list.add(build);
		return list;
	}

}
