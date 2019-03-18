package com.gx.ksw.entities;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.StringJoiner;

/**
 * 关键字实体
 * @author sgx
 * @version V1.0
 */
@ApiModel(description = "关键字实体")
public class KeyWord extends UtilEntity {
	/**
	 * 关键字的内容
	 */
	@NotNull(message = "请输入内容！")
	@Size(max = 100, message = "内容长度超过100个字符，请重新输入！")
	@ApiModelProperty(notes = "${keyWord.content}", example = "铁塔", required = true, position = 0)
	private String content;

	public KeyWord() {
	}

	public KeyWord(String createTime,
				   String modifyTime,
				   @NotNull(message = "请输入内容！")
				   @Size(max = 100, message = "内容长度超过100个字符，请重新输入！") String content) {
		super(createTime, modifyTime);
		this.content = content;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@Override
	public String toString() {
		return new StringJoiner(", ", KeyWord.class.getSimpleName() + "[", "]")
				.add("content='" + content + "'")
				.toString();
	}
}
