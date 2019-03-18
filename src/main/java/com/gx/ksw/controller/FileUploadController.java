package com.gx.ksw.controller;

import com.gx.ksw.exception.StorageFileNotFoundException;
import com.gx.ksw.server.StorageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.UnsupportedEncodingException;

/**
 * 文件下载
 */
@Controller
@Api(value = "FileUploadController", description = "文件上传下载的转发层")
public class FileUploadController {

	private final StorageService storageService;

	@Autowired
	public FileUploadController(StorageService storageService) {
		this.storageService = storageService;
	}

	/**
	 * 文件下载
	 *
	 * @param filename
	 * @return
	 */
	@GetMapping("/files/{filename:.+}")
	@ResponseBody
	@ApiOperation(value = "文件的下载",notes = "文件的下载", httpMethod = "GET",
			produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<Resource> serveFile(@ApiParam("要下载的文件的名称")
												  @PathVariable String filename) throws UnsupportedEncodingException {
		Resource file = storageService.loadAsResource(filename);
		return ResponseEntity.ok()
				.header(
						HttpHeaders.CONTENT_DISPOSITION,
						"attachment; filename=\"" +
								// 只有火狐是这各解决方式
								// 其它浏览器是 URLEncoder.encode(file.getFilename(), "UTF-8")
								new String(
										file.getFilename().getBytes("UTF-8"),
										"iso-8859-1"
								) + "\""
				)
				.body(file);
	}

	@ApiOperation(value = "文件夹没有找到，或者失败异常")
	@ExceptionHandler(StorageFileNotFoundException.class)
	public ResponseEntity<?> handleStorageFileNotFound(StorageFileNotFoundException exc) {
		return ResponseEntity.notFound().build();
	}
}
