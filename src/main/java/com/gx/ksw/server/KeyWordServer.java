package com.gx.ksw.server;

import java.util.List;

/**
 * 服务层总接口
 * @author sgx
 * @version V1.0
 * @param <T> 传入Bean的类型
 */

public interface KeyWordServer<T> {
	/**
	 * 根据ID查询Bean
	 *
	 * @param id
	 * @return
	 */
	T queryById(Long id);

	/**
	 * 查询所有的数据
	 *
	 * @return
	 */
	List<T> findAll();

	/**
	 * 根据内容模糊查询出所有的数据
	 *
	 * @param content
	 * @return
	 */
	List<T> findAllLike(String content);

	/**
	 * 增加Bean
	 *
	 * @param t
	 * @return
	 */
	int add(T t);

	/**
	 * 更新Bean
	 *
	 * @param t
	 * @return
	 */
	int update(T t);

	/**
	 * 根据ID删除Bean
	 *
	 * @param id
	 * @return
	 */
	int deleteById(Long id);
}
