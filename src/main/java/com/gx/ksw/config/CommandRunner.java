package com.gx.ksw.config;

import com.gx.ksw.server.StorageProperties;
import com.gx.ksw.server.StorageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * 自定义服务启动后执行的任务
 */
@EnableConfigurationProperties(StorageProperties.class)
@Component
public class CommandRunner {
	private static Logger logger = LoggerFactory.getLogger(CommandRunner.class);
	@Value("${spring.web.loginurl}")
	private String loginUrl;

	//	@Value("${spring.web.excute}")
	@Value("${spring.web.firefoxexcute}")
	private String excutePath;

	@Value("${spring.auto.openurl}")
	private boolean isOpen;

	/**
	 * 打开指定的浏览器，打开主界面
	 * @return
	 */
	// @Bean("runBrowser")
	public CommandLineRunner run() {
		return args -> {
			if (isOpen) {
				String cmd = excutePath + " " + loginUrl;
				Runtime run = Runtime.getRuntime();
				try {
					run.exec(cmd);
					logger.debug("启动浏览器打开项目成功");
				} catch (Exception e) {
					e.printStackTrace();
					logger.error(e.getMessage());
				}
			}
		};
	}

	@Bean(value = "fileInit")
	public CommandLineRunner init(StorageService storageService) {
		return (args) -> {
			storageService.deleteAll();
			storageService.init();
		};
	}
}