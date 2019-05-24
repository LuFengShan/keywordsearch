package com.gx.ksw.exception;

import java.util.Date;

/**
 * Spring Boot提供的默认错误响应包含通常需要的所有详细信息。
 * <p>
 * 但是，您可能希望为组织创建独立于框架的响应结构。在这种情况下，您可以定义特定的错误响应结构。
 *
 * @Author sgx
 * @Date 2019/5/24 10:28
 * @Version
 **/
public class ErrorDetails {
	private Date timestamp;
	private String message;
	private String details;

	public ErrorDetails(Date timestamp, String message, String details) {
		super();
		this.timestamp = timestamp;
		this.message = message;
		this.details = details;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public String getMessage() {
		return message;
	}

	public String getDetails() {
		return details;
	}
}
