package com.gx.ksw.controller;

import com.gx.ksw.wanwan.demo.InvokeRecordAnno;
import com.gx.ksw.wanwan.demo2.Demo2;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 进入主页
 */
@Controller
@Api(value = "HomeController", description = "主页控制或着退出")
public class HomeController {
	@Autowired
	ApplicationContext applicationContext;

	/**
	 * 自动来到主页
	 * @return
	 */
	@Demo2("代理测试2")
	@InvokeRecordAnno("测试代理模式")
	@RequestMapping("/")
	@ApiOperation("自动进入主页面")
	public String home(){
		return "home";
	}

	/**
	 * 退出SPRINGBOOT应用程序
	 */
	@GetMapping("/exit")
	@ApiOperation("主动退出程序")
	public void exitProject(){
		System.exit(SpringApplication.exit(applicationContext));
	}
}
