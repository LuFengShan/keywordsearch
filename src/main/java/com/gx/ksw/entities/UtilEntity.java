package com.gx.ksw.entities;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.StringJoiner;

/**
 * 所有存入数据库基类的父类
 */
public class UtilEntity {
	/**
	 * id
	 */
	private Long id;
	/**
	 * 创建时间
	 */
	@NotNull
	@Size(max = 20)
	private String createTime;
	/**
	 * 最后一次修改的时间
	 */
	@NotNull
	@Size(max = 20)
	private String modifyTime;

	public UtilEntity() {
	}

	public UtilEntity(String createTime, String modifyTime) {
		this.createTime = createTime;
		this.modifyTime = modifyTime;
	}

	@Override
	public String toString() {
		return new StringJoiner(", ", UtilEntity.class.getSimpleName() + "[", "]")
				.add("id=" + id)
				.add("createTime='" + createTime + "'")
				.add("modifyTime='" + modifyTime + "'")
				.toString();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getModifyTime() {
		return modifyTime;
	}

	public void setModifyTime(String modifyTime) {
		this.modifyTime = modifyTime;
	}
}
