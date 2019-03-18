package com.gx.ksw.entities;

/**
 * 文本搜索是把他当做一个对象，这样避免get方式传值数据太多的问题
 * @author sgx
 * @version V1.0
 */
public class Text {
    /**
     * 文本内容
     */
    private String textContent;

    /**
     * 选择的所有关键字
     */
    private String keyWordGroup;

    public String getTextContent() {
        return textContent;
    }

    public void setTextContent(String textContent) {
        this.textContent = textContent;
    }

    public String getKeyWordGroup() {
        return keyWordGroup;
    }

    public void setKeyWordGroup(String keyWordGroup) {
        this.keyWordGroup = keyWordGroup;
    }

    @Override
    public String toString() {
        return "Text{" +
                "textContent='" + textContent + '\'' +
                ", keyWordGroup='" + keyWordGroup + '\'' +
                '}';
    }
}
