package com.gx.ksw.server;

import com.gx.ksw.exception.StorageException;
import com.gx.ksw.exception.StorageFileNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

/**
 * 上传下载文件的初始化等
 *
 * @author sgx
 * @version V1.0
 */
@Service
public class FileSystemStorageService implements StorageService {
	private Logger logger = LoggerFactory.getLogger(FileSystemStorageService.class);

	private final Path rootLocation;

	/**
	 * 把上传的文件拷贝到指定位置
	 * <p>
	 * {@link Path#resolve(String)} : 将给定的路径字符串转换为路径，并以解析方法指定的方式针对该路径解析该字符串。
	 * 例如，假设名称分隔符为“/”，路径表示“foo/bar”，那么使用路径字符串“gus”调用此方法将导致路径“foo/bar/gus”。
	 * </p>
	 * <p>{@link StandardCopyOption#REPLACE_EXISTING} : 替换现有文件（如果存在）</p>
	 *
	 * @param file
	 */
	@Override
	public void store(MultipartFile file) {
		try {
			Files.copy(file.getInputStream(),
					this.rootLocation.resolve(file.getOriginalFilename()),
					StandardCopyOption.REPLACE_EXISTING);
		} catch (Exception e) {
			throw new RuntimeException("FAIL! -> message = " + e.getMessage());
		}
	}

	@Autowired
	public FileSystemStorageService(StorageProperties properties) {
		this.rootLocation = Paths.get(properties.getLocation());
	}

	@Override
	public Path load(String filename) {
		return rootLocation.resolve(filename);
	}

	/**
	 * 加载资源
	 */
	@Override
	public Resource loadAsResource(String filename) {
		logger.info("加载资源 ： " + filename);
		try {
			Path file = load(filename);
			Resource resource = new UrlResource(file.toUri()); // 根据给定的URI对象创建一个新的URLRealth.
			if (resource.exists() || resource.isReadable()) {
				return resource;
			} else {
				throw new StorageFileNotFoundException("Could not read file: " + filename);
			}
		} catch (MalformedURLException e) {
			throw new StorageFileNotFoundException("Could not read file: " + filename, e);
		}
	}

	/**
	 * 初始化所在的文件，删除指定文件中所有的文件
	 */
	@Override
	public void deleteAll() {
		FileSystemUtils.deleteRecursively(rootLocation.toFile());
	}

	/**
	 * 初始化来创建文件
	 */
	@Override
	public void init() {
		try {
			Files.createDirectories(rootLocation);
		} catch (IOException e) {
			throw new StorageException("不能初始化存储目录", e);
		}
	}
}
