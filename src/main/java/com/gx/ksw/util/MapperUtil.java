package com.gx.ksw.util;

import com.gx.ksw.entities.DockerAndKeyWord;
import com.gx.ksw.entities.KeyWord;
import com.gx.ksw.entities.KeyWordDocker;
import com.gx.ksw.entities.KeyWordMapContent;
import org.springframework.jdbc.core.RowMapper;

/**
 * jdbctemplate的row映射
 */
public class MapperUtil {

	/**
	 * 关键字库和关键字的映射
	 * @return
	 */
	public static RowMapper<DockerAndKeyWord> dockerAndKeyWorRowMapper() {
		return (rs, rowNum) -> {
			DockerAndKeyWord dockerAndKeyWord = new DockerAndKeyWord();
			dockerAndKeyWord.setDockerId(rs.getLong("docker_id"));
			dockerAndKeyWord.setKeyWordId(rs.getLong("keyword_id"));
			dockerAndKeyWord.setListKeyWordId(null);
			return dockerAndKeyWord;
		};

	}

	/**
	 * 关键字库的映射
	 *
	 * @return
	 */
	public static RowMapper<KeyWordDocker> keyWordDockerRowMapper() {
		return (rs, rowNum) -> {
			KeyWordDocker keyWordDocker = new KeyWordDocker();
			keyWordDocker.setId(rs.getLong("id"));
			keyWordDocker.setDockerName(rs.getString("docker_name"));
			keyWordDocker.setCreateTime(rs.getString("create_time"));
			keyWordDocker.setModifyTime(rs.getString("modify_time"));
			return keyWordDocker;
		};
	}

	/**
	 * 关键字的映射
	 * @return
	 */
	public static RowMapper<KeyWord> keyWordRowMapper() {
		return (rs, rowNum) -> {
			KeyWord keyword = new KeyWord();
			keyword.setId(rs.getLong("id"));
			keyword.setContent(rs.getString("content"));
			keyword.setCreateTime(rs.getString("create_time"));
			keyword.setModifyTime(rs.getString("modify_time"));
			return keyword;
		};
	}

	/**
	 * <p>关键字和内容的映射，也用作关键字和关键字库的映射</p>
	 * @return
	 */
	public static RowMapper<KeyWordMapContent> keyWordMapContentRowMapper() {
		return (rs, rowNum) -> {
			KeyWordMapContent kwmc = new KeyWordMapContent();
			kwmc.setContent(rs.getString(1));
			kwmc.setKeyWord(rs.getString(2));
			return kwmc;
		};
	}
}
