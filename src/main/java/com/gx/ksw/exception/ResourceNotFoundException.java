package com.gx.ksw.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * 我们可以为特定异常指定响应状态以及'@ResponseStatus'注释的异常定义。
 * @Author sgx
 * @Date 2019/5/24 10:26
 * @Version
 **/
@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends Exception{
	private static final long serialVersionUID = 1L;
	public ResourceNotFoundException(String message){
		super(message);
	}
}