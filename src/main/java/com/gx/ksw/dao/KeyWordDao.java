package com.gx.ksw.dao;

import java.util.List;

/**
 * <p>所有数据库交互层的公共接口</p>
 * @author sgx
 * @version V1.0
 * @param <T> 数据库对应的java对象
 */
public interface KeyWordDao<T> {
	/**
	 * 根据ID查询一个个对象
	 * @param id
	 * @return
	 */
	T queryById(Long id);

	/**
	 * 查询所有的对象
	 * @return
	 */
	List<T> findAll();

	/**
	 * 根据查询内容，查询出所有的对象
	 * @param content
	 * @return
	 */
	List<T> findAllLike(String content);

	/**
	 * 增加一个对象
	 * @param t
	 * @return
	 */
	int add(T t);

	/**
	 * 更新一个对象
	 * @param t
	 * @return
	 */
	int update(T t);

	/**
	 * 根据ID删除一个对象
	 * @param id
	 * @return
	 */
	int deleteById(Long id);
}
