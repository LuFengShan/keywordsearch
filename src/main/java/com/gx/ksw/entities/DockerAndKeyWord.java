package com.gx.ksw.entities;

import java.util.List;

/**
 * <p>关联关系, 关键字和关键字库是多对多的关系</p>
 * @author sgx
 * @version V1.0
 */
public class DockerAndKeyWord {
	/**
	 * 关键字库 ID
	 */
	private long dockerId;
	/**
	 * 关键字的 ID
	 */
	private long keyWordId;
	/**
	 * 关键字的 ID 的集合
	 */
	private List<Long> listKeyWordId;

	public long getDockerId() {
		return dockerId;
	}

	public void setDockerId(long dockerId) {
		this.dockerId = dockerId;
	}

	public long getKeyWordId() {
		return keyWordId;
	}

	public void setKeyWordId(long keyWordId) {
		this.keyWordId = keyWordId;
	}

	public List<Long> getListKeyWordId() {
		return listKeyWordId;
	}

	public void setListKeyWordId(List<Long> listKeyWordId) {
		this.listKeyWordId = listKeyWordId;
	}
}
