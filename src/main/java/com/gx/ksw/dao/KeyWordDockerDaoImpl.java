package com.gx.ksw.dao;

import com.gx.ksw.entities.DockerAndKeyWord;
import com.gx.ksw.entities.KeyWordDocker;
import com.gx.ksw.entities.KeyWordMapContent;
import com.gx.ksw.util.MapperUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 关键字库的dao层，和数据库交互
 *
 * @author sgx
 * @version V1.0
 */
@Repository
public class KeyWordDockerDaoImpl implements KeyWordDao<KeyWordDocker> {

	@Autowired
	JdbcTemplate jdbcTemplate;

	/**
	 * 根据ID来删除一个关键字库
	 *
	 * @param id
	 * @return
	 */
	@Override
	public KeyWordDocker queryById(Long id) {
		return jdbcTemplate.queryForObject(
				"SELECT t.id, t.docker_name, t.create_time, t.modify_time FROM tb_key_word_docker t WHERE t.id = ?;"
				, new Object[]{id}
				, MapperUtil.keyWordDockerRowMapper()
		);
	}

	/**
	 * 查询所有的关键字库
	 *
	 * @return
	 */
	@Override
	public List<KeyWordDocker> findAll() {
		return jdbcTemplate.query(
				"SELECT t.id, t.docker_name, t.create_time, t.modify_time FROM tb_key_word_docker t"
				, MapperUtil.keyWordDockerRowMapper()
		);
	}

	/**
	 * 模板查询所有的关键字库
	 *
	 * @param content
	 * @return
	 */
	@Override
	public List<KeyWordDocker> findAllLike(String content) {
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT t.id, t.docker_name, t.create_time, t.modify_time ");
		sb.append("FROM tb_key_word_docker t ");
		sb.append("WHERE t.docker_name LIKE %");
		sb.append(content);
		sb.append("%;");
		return jdbcTemplate.query(sb.toString(), MapperUtil.keyWordDockerRowMapper());
	}

	/**
	 * 增加一个关键字库
	 *
	 * @param keyWordDocker
	 * @return
	 */
	@Override
	public int add(KeyWordDocker keyWordDocker) {
		return jdbcTemplate.update(
				"insert into tb_key_word_docker (docker_name, create_time, modify_time) values (?, ?, ?);"
				, keyWordDocker.getDockerName()
				, keyWordDocker.getCreateTime()
				, keyWordDocker.getModifyTime()
		);
	}

	/**
	 * 更新一个关键字库
	 *
	 * @param keyWordDocker
	 * @return
	 */
	@Override
	public int update(KeyWordDocker keyWordDocker) {
		return jdbcTemplate.update(
				"update tb_key_word_docker set docker_name = ?, modify_time = ? where id = ?;"
				, keyWordDocker.getDockerName()
				, keyWordDocker.getModifyTime()
				, keyWordDocker.getId()
		);
	}

	/**
	 * 根据ID删除一个关键字库
	 *
	 * @param id
	 * @return
	 */
	@Override
	public int deleteById(Long id) {
		return jdbcTemplate.update(
				"delete from tb_key_word_docker where id = ?;"
				, id
		);
	}


	/**
	 * <p>
	 * 1.增加关键字库的关联保存操作
	 * 具体操作：
	 * <ul>
	 * <li>1.删除选择关键字库和关键字之间的所有的关系</li>
	 * <li>2.重新建立关键字库和关键字之间的关系</li>
	 * </ul>
	 * </p>
	 *
	 * @param dockerAndKeyWord
	 * @return
	 */
	public int updateDockerRelationshipAndKeyWord(DockerAndKeyWord dockerAndKeyWord) {
		// 利用jdbc批处理
		List<Object[]> batch = new ArrayList<>();
		long dockerId = dockerAndKeyWord.getDockerId();
		List<Long> listKeyWordId = dockerAndKeyWord.getListKeyWordId();
		for (Long keyWordId : listKeyWordId) {
			Object[] values = new Object[]{dockerId, keyWordId};
			batch.add(values);
		}
		int[] batchUpdate = jdbcTemplate.batchUpdate(
				"insert into tb_docker_key_word_relation (docker_id, key_word_id) values (?, ?);"
				, batch
		);
		return batchUpdate.length;
	}

	/**
	 * 根据关键字库的ID删除关键字库和关键字之间的关系
	 *
	 * @return
	 */
	public int deleteDockerIdAndKeyWordId(Long id) {
		return jdbcTemplate.update(
				"delete from tb_docker_key_word_relation where docker_id = ?;"
				, id
		);
	}

	/**
	 * <p>查询出关键字库所对应的所有的关键字</p>
	 *
	 * @param id 关键字库ID
	 * @return
	 */
	public List<Long> queryKeyWordsByDockerId(Long id) {
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT m.id FROM tb_docker_key_word_relation t ");
		sb.append("LEFT JOIN tb_key_word m ON t.key_word_id = m.id ");
		sb.append("WHERE t.docker_id = ?;");
		return jdbcTemplate.query(sb.toString(), new Object[]{id}, (rs, rowNum) -> rs.getLong("id"));
	}

	/**
	 * 添加功能判断是否重复
	 * @param keyWordDocker 查询条件载体
	 * @return 匹配的关键字库名称个数
	 * wanghuidong modify 2018-12-12 根据关键字库名称查询个数
	 */
	public int getKeywordDockerCountWithName(KeyWordDocker keyWordDocker) {
		Object[] args = {keyWordDocker.getDockerName()};
		return jdbcTemplate.queryForObject(
				"SELECT count(1) FROM tb_key_word_docker WHERE docker_name=?",
				args,
				Integer.class
		);
	}

	/**
	 * 修改功能判断是否重复
	 * @param keyWordDocker 查询条件载体
	 * @return 匹配的关键字库名称且!=id的数据个数
	 * wanghuidong modify 2018-12-12 根据ID和关键字名称查询个数
	 */
	public int getKeywordDockerCountWithId(KeyWordDocker keyWordDocker) {
		Object[] args = {keyWordDocker.getId(),keyWordDocker.getDockerName()};
		return jdbcTemplate.queryForObject(
				"select count(1) from tb_key_word_docker where id!=? and docker_name=?",
				args,
				Integer.class
		);
	}

	/**
	 * 查询出所有的关键字库和关键字之间的关系
	 *
	 * @return
	 */
	public List<KeyWordMapContent> arrayDockerNameAndContent(Long id) {
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT p.docker_name, m.content ");
		sb.append("FROM tb_docker_key_word_relation t ");
		sb.append("LEFT JOIN tb_key_word m ON t.key_word_id = m.id ");
		sb.append("LEFT JOIN tb_key_word_docker p ON t.docker_id = p.id ");
		sb.append("WHERE p.docker_name NOT NULL ");
		if (Objects.equals(null, id)) { // 说明是查询所有的值
			sb.append("ORDER BY t.docker_id;");
		} else {
			sb.append("AND p.id = ");
			sb.append(id);
			sb.append(";");
		}
		List<KeyWordMapContent> query = jdbcTemplate.query(sb.toString(), MapperUtil.keyWordMapContentRowMapper());
		return query;
	}

	/**
	 * 根据内容来查询一个关键字库
	 *
	 * @param content
	 * @return
	 */
	public KeyWordDocker queryByContent(String content) {
		return jdbcTemplate.queryForObject(
				"SELECT t.id, t.docker_name, t.create_time, t.modify_time FROM tb_key_word_docker t WHERE t.docker_name = ?;"
				, new Object[]{content}
				, MapperUtil.keyWordDockerRowMapper()
		);
	}
}
