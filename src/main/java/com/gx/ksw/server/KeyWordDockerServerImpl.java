package com.gx.ksw.server;

import com.gx.ksw.dao.KeyWordDockerDaoImpl;
import com.gx.ksw.entities.DockerAndKeyWord;
import com.gx.ksw.entities.KeyWordDocker;
import com.gx.ksw.entities.KeyWordMapContent;
import com.gx.ksw.util.FreemarkerUtil;
import com.gx.ksw.util.UtilTime;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 关键字库的服务层
 */

@Transactional
@Service
public class KeyWordDockerServerImpl implements KeyWordServer<KeyWordDocker> {
	private Logger logger = LoggerFactory.getLogger(KeyWordDockerServerImpl.class);

	@Autowired
	KeyWordDockerDaoImpl keyWordDockerDao;

	@Override
	public KeyWordDocker queryById(Long id) {
		return keyWordDockerDao.queryById(id);
	}

	@Override
	public List<KeyWordDocker> findAll() {
		return keyWordDockerDao.findAll();
	}

	@Override
	public List<KeyWordDocker> findAllLike(String content) {
		return keyWordDockerDao.findAllLike(content);
	}

	@Override
	public int add(KeyWordDocker keyWordDocker) {
		if (Objects.equals(0, keyWordDockerDao.getKeywordDockerCountWithName(keyWordDocker))) {
			String strTime = UtilTime.dateAndTime();
			keyWordDocker.setCreateTime(strTime);
			keyWordDocker.setModifyTime(strTime);
			return keyWordDockerDao.add(keyWordDocker);
		}
		return 0;
	}

	@Override
	public int update(KeyWordDocker keyWordDocker) {
		keyWordDocker.setModifyTime(UtilTime.dateAndTime());
		return keyWordDockerDao.update(keyWordDocker);
	}

	/**
	 * 业务层，根据关键字库ID删除关键字库
	 * <p>
	 * <ul>
	 * <li>1.首先删除关键字库和关键字之间的关系</li>
	 * <li>2.然后删除关键字库</li>
	 * <li>3.这个方法要开启事务，如果一个删除失败，则都要回滚</li>
	 * </ul>
	 * </p>
	 *
	 * @param id
	 * @return
	 */
	@Override
	public int deleteById(Long id) {
		keyWordDockerDao.deleteDockerIdAndKeyWordId(id);
		return keyWordDockerDao.deleteById(id);
	}

	/**
	 * 建立关键字库和关键字的关系
	 * <p>
	 * 1.如果有旧的关系，则先删除旧的关系;
	 * 2.然后增加新的关联关系
	 * </p>
	 *
	 * @return
	 */
	public int updateDockerRelationshipAndKeyWord(DockerAndKeyWord dockerAndKeyWord) {
		// 如果已经有关联关系了
		if (keyWordDockerDao.queryKeyWordsByDockerId(dockerAndKeyWord.getDockerId()).size() > 0) {
			// 根据关键字库的ID删除关键字库和关键字之间的关系
			logger.info("删除关键字库ID为 ： " + dockerAndKeyWord.getDockerId() + " 的关联关系");
			keyWordDockerDao.deleteDockerIdAndKeyWordId(dockerAndKeyWord.getDockerId());
		}
		// 如果没有关联关系就直接增加
		int i = keyWordDockerDao.updateDockerRelationshipAndKeyWord(dockerAndKeyWord);
		logger.info("更新关键字库和关键字的关系的条数 : " + i);
		return i;
	}

	/**
	 * 根据关键字库ID查询这个关键字库包含的所有的关键字的ID
	 *
	 * @param id
	 * @return
	 */
	public List<Long> queryKeyWordsByDockerId(Long id) {
		return keyWordDockerDao.queryKeyWordsByDockerId(id);
	}

	/**
	 * 添加关键字库和修改关键字库分别查询数据库是否存在重复数据
	 *
	 * @param keyWordDocker 查询条件载体
	 * @return 存在数据库查询到的个数
	 * wanghuidong modify 2018-12-12 完善此方法
	 */
	public int getKeywordDockerCount(KeyWordDocker keyWordDocker) {
		int count;
		//添加和修改操作分别获取个数
		if (keyWordDocker.getId() == null || keyWordDocker.getId() < 1) {//添加
			count = keyWordDockerDao.getKeywordDockerCountWithName(keyWordDocker);
		} else {//修改
			count = keyWordDockerDao.getKeywordDockerCountWithId(keyWordDocker);
		}
		return count;
	}

	/**
	 * 查询出所有的关键字库和关键字之间的关系
	 *
	 * @return
	 */
	public Map<String, String> arrayDockerNameAndContent(Long id) {
		// 查出所有的关键字库和关键字之间的关系
		List<KeyWordMapContent> keyWordMapContents = keyWordDockerDao.arrayDockerNameAndContent(id);
		// 根据关键字库分组，重新整合成一个map
		Map<String, String> collect = keyWordMapContents.stream()
				.collect(Collectors.groupingBy(
						KeyWordMapContent::getContent, // 关键字库
						Collectors.mapping( // 把关键字用","给拼接起来
								KeyWordMapContent::getKeyWord,
								Collectors.joining(",")
						)
				));
		return collect;
	}

	public Long queryByContent(String content) {
		KeyWordDocker keyWordDocker = keyWordDockerDao.queryByContent(content);
		return keyWordDocker.getId();
	}

	/**
	 * @return
	 */
	public Map<String, List<String>> mapList() {
		// 查出所有的关键字库和关键字之间的关系
		List<KeyWordMapContent> keyWordMapContents = keyWordDockerDao.arrayDockerNameAndContent(null);
		// 根据关键字库分组，重新整合成一个map
		Map<String, List<String>> collect = keyWordMapContents.stream()
				.collect(Collectors.groupingBy(KeyWordMapContent::getContent,
						Collectors.mapping(
								KeyWordMapContent::getKeyWord,
								Collectors.toList()
						)));
		return collect;
	}

	/**
	 * 导出生成的检索结果
	 *
	 * @param listMap
	 * @return
	 */
	public String exportSearchFileResult(Map<String, List<KeyWordMapContent>> listMap) {
		// 1.模板文件名称
//		String templateLocation = "import_search.ftlh";
		//wanghuidong modify 2018-12-17 替换为mht文件改名的ftl模板文件.用于关键字底色标注
		String templateLocation = "import_search.ftl";
		// 2.生成文件的位置(xml,doc,html,htm)
		String fileLocation = "upload-dir/关键字检索结果.doc";
		// 3.获取一个配置文件
		Configuration cfg = new FreemarkerUtil().createConfigurationInstance("/");
		// 4.封装数据
		Map<String, Object> root = new HashMap<>();
		// 4.1 文件名称--因无法获取文件实际路径暂时取消显示
//		FreemarkerUtil.createDataModel(root, "filePath", "laal.doc");
		// 4.2 检索结果
		//wanghuidong modify 2018-12-10 因页面关键字底色,添加了HTML代码,下载文件为word文档,需要删除HTML代码
		FreemarkerUtil.createDataModel(root, "listMap", changeHtml2Mht(listMap));
		// 4.3 导出时间
		LocalDateTime dateTime = LocalDateTime.now();
		String importTime = dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
		FreemarkerUtil.createDataModel(root, "importTime", importTime);
		// 5.加载模板
		Template temp = FreemarkerUtil.getTemplate(cfg, templateLocation);
		// 6.合并模板和数据模型
		return FreemarkerUtil.mergingTemplateWithDataModel(root, temp, fileLocation);
	}

	/**
	 * 替换map中结果集的html代码为word文档的mht语言,用于导出检测结果文件中关键字标注黄色
	 * 因页面关键字底色,添加了HTML代码,下载文件为word文档,需要删除HTML代码
	 *
	 * @param listMap
	 * @return 去除html代码后的map
	 * wanghuidong modify 2018-12-10
	 */
	public Map<String, List<KeyWordMapContent>> changeHtml2Mht(Map<String, List<KeyWordMapContent>>
																	   listMap) {
		//替换所有黄色底色标签
		//		Map<String, List<KeyWordMapContent>> newListMap = new HashMap<>();
		//		listMap.forEach((key, value) -> newListMap.put(key, value.stream()
		//				.map(e -> new KeyWordMapContent(e.getKeyWord(), e.getContent()
		//						.replaceAll("<span style='background-color:#ffff00'>",
		//								"<span style=3D'background:yellow;mso-highlight:yellow'>")
		//				)).collect(Collectors.toList())
		//		));
		// sgx 根据业务1.取出listMap所有的value，2.合成一个流，3.把流中的每一个值进行替换，4.收集成一个集合，5.根据关键字进行分组
		Map<String, List<KeyWordMapContent>> newListMap = listMap.entrySet()
				.stream()
				.map(k -> k.getValue())
				.flatMap(Collection::stream)
				.map(e -> new KeyWordMapContent(e.getKeyWord(), e.getContent()
						.replaceAll("<span style='background-color:#ffff00'>",
								"<span style=3D'background:yellow;mso-highlight:yellow'>")))
				.collect(Collectors.groupingBy(KeyWordMapContent::getKeyWord));
		return newListMap;
	}

}
