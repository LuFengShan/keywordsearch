package com.gx.ksw.controller;

import com.alibaba.fastjson.JSONObject;
import com.gx.ksw.entities.DockerAndKeyWord;
import com.gx.ksw.entities.KeyWordDocker;
import com.gx.ksw.entities.KeyWordMapContent;
import com.gx.ksw.server.KeyWordDockerServerImpl;
import com.gx.ksw.server.KeyWordServerImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ResponseHeader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 关键字库的维护
 */
@Controller
@Api(value = "KeyWordDockerController", tags = "关键字库转发层")
public class KeyWordDockerController {
	private Logger logger = LoggerFactory.getLogger(KeyWordDockerController.class);

	@Autowired
	KeyWordDockerServerImpl keyWordDockerServer;

	@Autowired
	KeyWordServerImpl keyWordServer;

	@ResponseBody
	@GetMapping("kwd/{id}")
	public KeyWordMapContent dockerRelevanceKeyWord(@PathVariable Long id) {
		Map<String, String> map = keyWordDockerServer.arrayDockerNameAndContent(id);
		KeyWordMapContent kwmc = new KeyWordMapContent();
		Iterator<Map.Entry<String, String>> iter = map.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry<String, String> entry = iter.next();
			kwmc.setKeyWord(entry.getKey());
			kwmc.setContent(entry.getValue());
		}
		return kwmc;
	}

	/**
	 * 查询所有的关键字仓库
	 *
	 * @param model
	 * @return
	 */
	@GetMapping("/dockers")
	public String findAllKeyWordDocker(Model model, @RequestHeader MultiValueMap<String, String> headers) {
		headers.forEach((key, value) -> {
			logger.info(String.format("Header '%s' = %s", key, value.stream().collect(Collectors.joining("|"))));
		});
		model.addAttribute("dockers", keyWordDockerServer.findAll());
		return "docker/list";
	}

	/**
	 * 根据关键字仓库内容模糊查询一些数据
	 *
	 * @param content
	 * @param model
	 * @return
	 */
	@PostMapping("/docker/like/{content}")
	public String findAllKeyWordLikeById(@PathVariable("content") String content, Model model) {
			model.addAttribute("dockers", keyWordDockerServer.findAllLike(content));
		return "keyword/list";
	}


	/**
	 * 来到修改页面，查出当前关键字仓库，在页面回显
	 *
	 * @param id
	 * @param model
	 * @return
	 */
	@GetMapping("/docker/{id}")
	public String toEditKeyWordDockerPage(@PathVariable("id") Long id, Model model) {
		// 1. 回显关键字库的信息
		model.addAttribute("docker", keyWordDockerServer.queryById(id));
		// 2. 列出所有关键字信息
		model.addAttribute("keywords", keyWordServer.findAll());
		// 3. 带出关键字库和关键字的关联关系
		List<Long> longs = keyWordDockerServer.queryKeyWordsByDockerId(id);
		model.addAttribute("relationship", longs);
		String collect = longs.stream()
				.map(l -> String.valueOf(l))
				.collect(Collectors.joining(","));
		model.addAttribute("listKeyWordId", collect);
		// 回到修改页面(add是一个修改添加二合一的页面);
		return "docker/add";
	}

	/**
	 * 更新关键字仓库信息
	 *
	 * @param keyWordDocker
	 * @return
	 */
	@PutMapping("/docker")
	public String updateKeyWordDocker(KeyWordDocker keyWordDocker, DockerAndKeyWord dockerAndKeyWord) {
		// 1.更新关键字
		keyWordDockerServer.update(keyWordDocker);
		// 2.更新关键字库中的关系
		dockerAndKeyWord.setDockerId(keyWordDocker.getId());
		keyWordDockerServer.updateDockerRelationshipAndKeyWord(dockerAndKeyWord);
		return "redirect:/dockers";
	}

	/**
	 * 来到关键字仓库添加页面
	 *
	 * @return
	 */
	@GetMapping("/docker")
	public String toAddKeyWordDockerPage(Model model) {
		model.addAttribute("keywords", keyWordServer.findAll());
		return "docker/add";
	}

	/**
	 * 添加关键字仓库
	 *
	 * @param keyWordDocker
	 * @return
	 */
	@PostMapping("/docker")
	public String addKeyWordDocker(KeyWordDocker keyWordDocker, DockerAndKeyWord dockerAndKeyWord) {
		// 1.增加关键字库
		keyWordDockerServer.add(keyWordDocker);
		// 2.查出插入的关键字库的ID
		Long keyId = keyWordDockerServer.queryByContent(keyWordDocker.getDockerName());
		// 2.增加关联关系
		dockerAndKeyWord.setDockerId(keyId);
		keyWordDockerServer.updateDockerRelationshipAndKeyWord(dockerAndKeyWord);
		return "redirect:/dockers";
	}

	/**
	 * 根据ID删除关键字仓库
	 * <p>删除关键字库的同时，也要删除这个关键字库和关键字之间的关联关系</p>
	 *
	 * @param id
	 * @return
	 */
	@DeleteMapping("/docker/{id}")
	public String deleteKeyWordDocker(@PathVariable("id") Long id) {
		keyWordDockerServer.deleteById(id);
		return "redirect:/dockers";
	}

	/**
	 * 查看表中是否存在关键字库名称,没有返回0，存在则返回1
	 * @param jsonData
	 * @return
	 * wanghuidong modify 2018-12-12 修改此方法逻辑
	 */
	@GetMapping("/docker/getCount/{jsonData}")
	@ResponseBody
	@ApiOperation ("查询表中是否存在关键字库")
	public int getKeyWordDockerCount(@PathVariable String jsonData){
		try {
			jsonData = URLDecoder.decode(jsonData,"UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		KeyWordDocker keyWordDocker = JSONObject.parseObject(jsonData,KeyWordDocker.class);
		int count = keyWordDockerServer.getKeywordDockerCount(keyWordDocker);
		if (Objects.equals(0, count)){
			return 0;//可以保存
		} else {
			return 1;//重复存在
		}
	}

}
