package com.gx.ksw.entities;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.StringJoiner;

/**
 * 关键字库
 * @author sgx
 * @version V1.0
 */
public class KeyWordDocker extends UtilEntity{
	/**
	 * 关键字库名字
	 */
	@NotNull(message = "请输入‘关键字库名称’！")
	@Size(max = 50, message = "‘关键字库名称’超长，请重新输入（最多50个汉字）！")
	private String dockerName;

	public KeyWordDocker() {
	}

	public KeyWordDocker(String createTime, String modifyTime, @NotNull(message = "请输入‘关键字库名称’！") @Size(max = 50, message = "‘关键字库名称’超长，请重新输入（最多50个汉字）！") String dockerName) {
		super(createTime, modifyTime);
		this.dockerName = dockerName;
	}

	public String getDockerName() {
		return dockerName;
	}

	public void setDockerName(String dockerName) {
		this.dockerName = dockerName;
	}

	@Override
	public String toString() {
		return new StringJoiner(", ", KeyWordDocker.class.getSimpleName() + "[", "]")
				.add("dockerName='" + dockerName + "'")
				.toString();
	}
}
