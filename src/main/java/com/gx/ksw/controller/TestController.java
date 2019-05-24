package com.gx.ksw.controller;

import com.gx.ksw.exception.ResourceNotFoundException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @Author sgx
 * @Date 2019/5/24 10:49
 * @Version
 **/
@Api(value = "全局异常测试", description = "测试全局异常")
@Controller
@RequestMapping("/test")
public class TestController {
	/**
	 * 测试全局异常
	 *
	 * @throws ResourceNotFoundException
	 */
	@ApiOperation(value = "异常测试接口")
	@GetMapping("/testController")
	public void testController() throws ResourceNotFoundException {
		throw new ResourceNotFoundException("测试出错了");
	}
}
