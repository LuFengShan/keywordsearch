package com.gx.ksw;

import com.gx.ksw.entities.KeyWord;
import com.gx.ksw.util.MapperUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.stream.Stream;

@RunWith(SpringRunner.class)
@SpringBootTest
public class KeyWordSearchApplicationTests {
	private static Logger logger = LoggerFactory.getLogger(KeyWordSearchApplicationTests.class);

	@Autowired
	JdbcTemplate jdbcTemplate;

	@Autowired
	ApplicationContext applicationContext;

	@Test
	public void printBeans(){
		String[] beanDefinitionNames = applicationContext.getBeanDefinitionNames();
		Stream.of(beanDefinitionNames)
				.filter(s -> !s.contains("."))
				.sorted()
				.forEach(System.out::println);
	}

	@Test
	public void selectKeyword() {
		List<KeyWord> list = jdbcTemplate.query(
				"SELECT t.id, t.content, t.create_time, t.modify_time FROM tb_key_word t"
				, MapperUtil.keyWordRowMapper());
		list.forEach(System.out::println);
	}

	/**
	 * 查询出关键字库所对应的所有的关键字
	 */
	@Test
	public void queryKeyWordsByDockerId() {
		String sql = "select m.id from tb_docker_key_word_relation t left join tb_key_word m on t.key_word_id = m.id where t.docker_id = ?";
		List<Long> list = jdbcTemplate.query(sql, new Object[]{10L}, (rs, rowNum) -> rs.getLong("id"));
		logger.info(list.size() + "");
		list.stream().forEach(System.out::println);
	}

	@Test
	public void queryByContent() {
		String content = "lalalalal";
		int countOfContent  = jdbcTemplate.queryForObject("SELECT count(*) FROM tb_key_word t WHERE t.content = ?", Integer.class, content);
		logger.info("查询到的条数 ： " + countOfContent);
	}

}
