package com.gx.ksw.controller;

import com.alibaba.fastjson.JSONObject;
import com.gx.ksw.entities.KeyWord;
import com.gx.ksw.server.KeyWordServerImpl;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;
import java.util.Objects;

@Controller
@Api(value = "KeyWordController", description = "关键字转发层")
public class KeyWordController {

	@Autowired
	KeyWordServerImpl keyWordServer;

	/**
	 * 查询所有的关键字
	 * @param model
	 * @return
	 */
	@GetMapping("/keywords")
	@ApiOperation(value = "查看所有用户", notes = "查看所有用户", httpMethod = "GET")
	public String findAllKeyWord(@ApiParam("返回所有的关键字") Model model) {
		List<KeyWord> all = keyWordServer.findAll();
		model.addAttribute("keywords", all);
		return "keyword/list";
	}

	/**
	 * 根据关键字内容模糊查询一些数据
	 * @param content
	 * @param model
	 * @return
	 */
	@PostMapping("/keyword/like/{content}")
	@ApiOperation(value = "模糊查询返回查询到的所有的关键字", httpMethod = "POST")
	public String findProductById(@ApiParam("传入的内容") @RequestParam("content") String content, Model model) {
		List<KeyWord> list = keyWordServer.findAllLike(content);
		model.addAttribute("keywords", list);
		return "keyword/list";
	}


	/**
	 * 来到修改页面，查出当前关键字，在页面回显
	 * @param id
	 * @param model
	 * @return
	 */
	@GetMapping("/keyword/{id}")
	@ApiOperation(value = "修改关键字", notes = "修改关键字", httpMethod = "GET")
	public String toEditPage(@ApiParam("关键字的ID") @PathVariable("id") Long id,Model model){
		KeyWord keyWord = keyWordServer.queryById(id);
		model.addAttribute("keyword",keyWord);
		//回到修改页面(add是一个修改添加二合一的页面);
		return "keyword/add";
	}

	/**
	 * 更新关键字信息
	 *
	 * @param keyWord
	 * @return
	 */
	@PutMapping("/keyword")
	@ApiOperation(value = "更新关键字", notes = "更新关键字", httpMethod = "PUT")
	public String updateKeyWord(KeyWord keyWord) {
		keyWordServer.update(keyWord);
		return "redirect:/keywords";
	}

	/**
	 * 来到关键字添加页面
	 * @return
	 */
	@GetMapping("/keyword")
	@ApiOperation(value = "添加关键字", notes = "添加关键字", httpMethod = "GET")
	public String toAddKeyWordPage() {
		return "keyword/add";
	}

	/**
	 * 添加关键字
	 *
	 * @param keyWord
	 * @return
	 */
	@PostMapping("/keyword")
	@ApiOperation(value = "添加关键字", notes = "添加关键字", httpMethod = "POST")
	public String addKeyWord(KeyWord keyWord) {
		keyWordServer.add(keyWord);
		return "redirect:/keywords";
	}

	/**
	 * 根据ID删除关键字
	 * @param id
	 * @return
	 */
	@ApiOperation(value = "删除用户", notes = "删除用户")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "id", value = "关键字标识", required = true, dataType = "Long")
	})
	@DeleteMapping("/keyword/{id}")
	public String deleteEmployee(@PathVariable("id") Long id) {
		keyWordServer.deleteById(id);
		return "redirect:/keywords";
	}

	/**
	 * 查看表中有几个这样的关键字,没有返回0，存在则返回1
	 * @param jsonData
	 * @return
	 * wanghuidong modify 2018-12-11 修改此方法逻辑
	 */
	@GetMapping("/keyword/getCount/{jsonData}")
	@ResponseBody
	@ApiOperation("查询表中是否存在关键字")
	public int getKeywordCount(@PathVariable String jsonData){
		try {
			jsonData = URLDecoder.decode(jsonData,"UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		KeyWord keyWordVo = JSONObject.parseObject(jsonData,KeyWord.class);
		int count = keyWordServer.getKeywordCount(keyWordVo);
		if (Objects.equals(0, count)){
			return 0;//可以保存
		} else {
			return 1;//重复存在
		}
	}

}
