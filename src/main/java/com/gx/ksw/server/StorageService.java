package com.gx.ksw.server;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;

public interface StorageService {
    /**
     * 把文件上传到指定位置
     * @param file
     */
    void store(MultipartFile file);

    void init();

    Path load(String filename);

    Resource loadAsResource(String filename);

    void deleteAll();

}
