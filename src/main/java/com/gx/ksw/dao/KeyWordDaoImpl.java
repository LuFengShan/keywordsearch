package com.gx.ksw.dao;

import com.gx.ksw.entities.KeyWord;
import com.gx.ksw.util.MapperUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 关键字数据处理层
 *
 * @author sgx
 * @version V1.0
 */
@Repository
public class KeyWordDaoImpl implements KeyWordDao<KeyWord> {
	private static Logger logger = LoggerFactory.getLogger(KeyWordDaoImpl.class);

	@Autowired
	JdbcTemplate jdbcTemplate;

	/**
	 * <p>根据ID查询对应的关键字</p>
	 *
	 * @param id id
	 * @return
	 */
	@Override
	public KeyWord queryById(Long id) {
		KeyWord keyword = jdbcTemplate.queryForObject(
				"SELECT t.id, t.content, t.create_time, t.modify_time FROM tb_key_word t WHERE t.id = ?"
				, new Object[]{id}
				, MapperUtil.keyWordRowMapper());
		logger.info(keyword.toString());
		return keyword;
	}

	/**
	 * <p>获取所有的关键字</p>
	 *
	 * @return List<KeyWord> 关键字集合
	 */
	@Override
	public List<KeyWord> findAll() {
		return jdbcTemplate.query(
				"SELECT t.id, t.content, t.create_time, t.modify_time FROM tb_key_word t;"
				, MapperUtil.keyWordRowMapper()
		);
	}

	/**
	 * <p>根据提交的内容，模糊查询匹配的所有的关键字</p>
	 *
	 * @param content 内容
	 * @return List<KeyWord> 关键字集合
	 */
	@Override
	public List<KeyWord> findAllLike(String content) {
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT t.id, t.content, t.create_time, t.modify_time FROM tb_key_word t ");
		sb.append("WHERE t.content LIKE %");
		sb.append(content);
		sb.append("%;");
		return jdbcTemplate.query(sb.toString(), MapperUtil.keyWordRowMapper());
	}

	/**
	 * <p>插入一个关键字</p>
	 *
	 * @param keyWord
	 * @return 数据库影响的行数
	 */
	@Override
	public int add(KeyWord keyWord) {
		return jdbcTemplate.update(
				"insert into tb_key_word (content, create_time, modify_time) values (?, ?, ?);"
				, keyWord.getContent()
				, keyWord.getCreateTime()
				, keyWord.getModifyTime()
		);
	}

	/**
	 * <p>更新关键字</p>
	 *
	 * @param keyWord
	 * @return 数据库影响的行数
	 */
	@Override
	public int update(KeyWord keyWord) {
		return jdbcTemplate.update(
				"update tb_key_word set content = ?, modify_time = ? where id = ?;"
				, keyWord.getContent()
				, keyWord.getModifyTime()
				, keyWord.getId()
		);
	}

	/**
	 * <p>根据id删除关键字</p>
	 *
	 * @param id id
	 * @return 数据库影响的行数
	 */
	@Override
	public int deleteById(Long id) {
		// 1. 删除关键字库和关键字之间的关系
		jdbcTemplate.update(
				"delete from tb_docker_key_word_relation where key_word_id = ?;"
				, id
		);
		// 2. 删除关键字
		int count = jdbcTemplate.update("delete from tb_key_word where id = ?;", id);
		logger.info(count + " : " + id);
		return count;
	}


	/**
	 * 修改功能判断是否重复
	 * @param keyWord 查询条件载体
	 * @return 匹配的关键字且!=id的数据个数
	 * wanghuidong modify 2018-12-11 根据ID和内容查询个数
	 */
	public int getKeywordCountWithId(KeyWord keyWord) {
		Object[] args = {keyWord.getId(),keyWord.getContent()};
		return jdbcTemplate.queryForObject(
				"select count(1) from tb_key_word where id!=? and content=?",
				args,
				Integer.class
		);
	}

	/**
	 * 添加功能查判断是否重复
	 * @param keyWord 查询条件载体
	 * @return 匹配的关键字个数
	 * wanghuidong modify 2018-12-11 根据关键字查询个数
	 */
	public int getKeywordCountWithContent(KeyWord keyWord) {
		Object[] args = {keyWord.getContent()};
		return jdbcTemplate.queryForObject(
				"SELECT count(1) FROM tb_key_word t WHERE t.content = ?",
				args,
				Integer.class
		);
	}
}
