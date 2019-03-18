package com.gx.ksw.controller;

import com.gx.ksw.entities.Text;
import com.gx.ksw.server.KeyWordDockerServerImpl;
import com.gx.ksw.server.KeyWordServerImpl;
import com.gx.ksw.util.TxtUtil;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

/**
 * 关键字控制层
 */
@Controller
@Api(value = "KeyWordSearchController", description = "关键字搜索转发层")
public class KeyWordSearchController {
	Logger log = LoggerFactory.getLogger(KeyWordSearchController.class);
	@Autowired
	KeyWordServerImpl keyWordServer;

	@Autowired
	KeyWordDockerServerImpl keyWordDockerServer;

	/**
	 * @param model
	 * @return
	 */
	@GetMapping("/paragraphs")
	public String forWardKeyWordSearch(Model model) {
		// 把关键字和关键字库都装进来
		model.addAttribute("keywords", keyWordServer.findAllKeyWordContent());
		model.addAttribute("dockerMapKeyWords", keyWordDockerServer.arrayDockerNameAndContent(null));
		return "retrieval/paragraphs";
	}

	/**
	 * @param text
	 * @param model
	 * @return
	 */
	@PostMapping("/paragraphs")
	public String subKeyWordSearch(Text text, Model model) {
		// 判断是关键字库
		List<String> keyWords;
		if (text.getKeyWordGroup().startsWith("[{")) {
			keyWords = TxtUtil.listBoostrapTree(text.getKeyWordGroup());
		} else {
			keyWords = TxtUtil.parserKeyWord(text.getKeyWordGroup());
		}
		// 1. 筛选出所有的关键字，其他属性不要
		String textContent = text.getTextContent();
		if (textContent.contains("<span style=\"background-color: yellow\">")) {
			textContent = textContent.replace("<span style=\"background-color: yellow\">", "");
		}
		if (textContent.contains("</span>")) {
			textContent = textContent.replace("</span>", "");
		}
		for (String key : keyWords) {
			textContent = textContent.replace(key, "<span style=\"background-color: yellow\">" + key + "</span>");
		}
		text.setTextContent(textContent);
		System.out.println("检索关键字方法post");
		model.addAttribute("text", text);
//		// 3.关键字集合要回显
//		model.addAttribute("keywords", keyWordServer.findAllKeyWordContent());
//		// 4. 关键字库也要回显
//		model.addAttribute("dockerMapKeyWords", keyWordDockerServer.arrayDockerNameAndContent(null));
		return "retrieval/paragraphs_result";
	}

}
