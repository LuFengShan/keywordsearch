package com.gx.ksw.server;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 这是一个配置类，主要是用来管理上传下载的文件的
 * @author sgx
 * @version V1.0
 */
@ConfigurationProperties("storage")
public class StorageProperties {

    /**
     * Folder location for storing files
     */
    private String location = "upload-dir";

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

}
