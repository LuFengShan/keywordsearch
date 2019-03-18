package com.gx.ksw.controller;

import com.gx.ksw.entities.KeyWordMapContent;
import com.gx.ksw.entities.Text;
import com.gx.ksw.server.DocumentSearchServerImpl;
import com.gx.ksw.server.KeyWordDockerServerImpl;
import com.gx.ksw.server.KeyWordServerImpl;
import com.gx.ksw.server.StorageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.*;

/**
 * <p>
 * 文件内容检索转发层。1.选择文件的路径；2.选择关键字库或关键字;3.检索；4.导出结果（可选）
 * </p>
 *
 * @author sgx
 * @version V1.0
 */
@Controller
@Api(value = "DocumentSearchController", description = "文件内容中的关键字的搜索")
public class DocumentSearchController {
	private static Logger log = LoggerFactory.getLogger(DocumentSearchController.class);

	@Autowired
	DocumentSearchServerImpl documentSearchServer;
	@Autowired
	KeyWordServerImpl keyWordServer;
	@Autowired
	KeyWordDockerServerImpl keyWordDockerServer;
	@Autowired
	StorageService storageService;

	/**
	 * 第一次请求这个页面的时候要把关键字和关键字库的内容都带过来
	 *
	 * @param model
	 * @return
	 */
	@GetMapping("/files")
	@ApiOperation("打开这个页面时，要带着关键字和关键字库")
	public String forWardDocumentSearch(@ApiParam("model模型") Model model) {
		// 1..关键字集合
		List<String> keywords = keyWordServer.findAllKeyWordContent();
		model.addAttribute("keywords", keywords);
		// 2.. 关键字库集合
		Map<String, String> dockerMapKeyWords = keyWordDockerServer.arrayDockerNameAndContent(null);
		model.addAttribute("dockerMapKeyWords", dockerMapKeyWords);
		return "retrieval/files";
	}

	@GetMapping("/zip")
	public String zip() {
		return "uploadform";
	}

	@PostMapping("/zip")
	public void uploadZip(MultipartFile file) {
		storageService.store(file);
	}

	/**
	 * <p>上传文件读取，文件检索完成以后，把检索结果直接写入系统指定的文件中，这样在导出结果的时候就把生成的文件直接复制到本地指定的文件</p>
	 *
	 * @param file  上传的文件
	 * @param text  包含关键字
	 * @param model 处理完以后封装成对象
	 * @return
	 * @throws IOException
	 */
	@PostMapping("/files")
	@ApiOperation("返回文件检索后的结果，这个结果已经生成前已经生成了要导出的检索结果的文件")
	public String documentUpload(@ApiParam("上传的文件") @NotNull MultipartFile file,
								 @ApiParam("传入的关键字") Text text,
								 @ApiParam("model模型") Model model) {
		Map<String, List<KeyWordMapContent>> listMap = documentSearchServer.searchFile(file, text.getKeyWordGroup());
		// 0.把要下载的文件先写入本地硬盘中
		String filePath = keyWordDockerServer.exportSearchFileResult(listMap);
		log.info("导出文件的结果的路径 ： " + filePath);
		// 1.把包含关键字的内容的集合放入map中
		model.addAttribute("listMap", listMap);
		// 2.文件要回显
		model.addAttribute("fileName", "文件名称 ： " + file.getOriginalFilename());
		// 3.关键字集合要回显
		model.addAttribute("keywords", keyWordServer.findAllKeyWordContent());
		// 4. 关键字库也要回显
		model.addAttribute("dockerMapKeyWords", keyWordDockerServer.arrayDockerNameAndContent(null));
		// 5. 文件要回显
		return "retrieval/files_result";
	}

	@ResponseBody
	@PostMapping(value = "/queryKeyWordDocker", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<Object> queryKeyWordDocker() {
		Map<String, List<String>> mapList = keyWordDockerServer.mapList();
		List<Object> result = new ArrayList<>();
		Iterator<Map.Entry<String, List<String>>> iter = mapList.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry<String, List<String>> entry = iter.next();
			String key = entry.getKey();
			Map<String, Object> map = new HashMap<>();
			map.put("text", key);
			//盛放关键字
			List<Object> cList = new ArrayList<>();
			List<String> value = entry.getValue();
			for (String str : value) {
				Map<String, Object> cMap = new HashMap<>();
				cMap.put("text", str);
				cList.add(cMap);
			}
			map.put("nodes", cList);
			result.add(map);
		}

		return result;
	}

}
