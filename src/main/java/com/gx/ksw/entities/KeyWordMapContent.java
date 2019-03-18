package com.gx.ksw.entities;

/**
 * <p>关键字和内容的映射关系,内容中包含关键字。</p>
 * @author sgx
 * @version V1.0
 */
public class KeyWordMapContent {
	/**
	 * 关键字
	 */
	private String keyWord;
	/**
	 * 对应的内容
	 */
	private String content;

	public String getKeyWord() {
		return keyWord;
	}

	public void setKeyWord(String keyWord) {
		this.keyWord = keyWord;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public KeyWordMapContent() {
	}

	public KeyWordMapContent(String keyWord, String content) {
		this.keyWord = keyWord;
		this.content = content;
	}

	@Override
	public String toString() {
		return "KeyWordMapContent{" +
				"keyWord='" + keyWord + '\'' +
				", content='" + content + '\'' +
				'}';
	}
}
