package com.gx.ksw.server;

import com.gx.ksw.dao.KeyWordDaoImpl;
import com.gx.ksw.entities.KeyWord;
import com.gx.ksw.util.UtilTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 关键字服务层
 * @author sgx
 * @version V1.0
 */
@Transactional
@Service
public class KeyWordServerImpl implements KeyWordServer<KeyWord> {
	@Autowired
	KeyWordDaoImpl daoRepository;

	@Override
	public KeyWord queryById(Long id) {
		return daoRepository.queryById(id);
	}

	@Override
	public List<KeyWord> findAll() {
		return daoRepository.findAll();
	}

	@Override
	public List<KeyWord> findAllLike(String content) {
		return daoRepository.findAllLike(content);
	}

	@Override
	public int add(KeyWord keyWord) {
		// 判断这表关键字在数据库中是不是已经存在了，如果存在了就不增加了
		if (Objects.equals(1, daoRepository.getKeywordCountWithContent(keyWord))) {
			return 0;
		} else {
			String strTime = UtilTime.dateAndTime();
			keyWord.setCreateTime(strTime);
			keyWord.setModifyTime(strTime);
			return daoRepository.add(keyWord);
		}
	}

	@Override
	public int update(KeyWord keyWord) {
		keyWord.setModifyTime(UtilTime.dateAndTime());
		return daoRepository.update(keyWord);
	}

	@Override
	public int deleteById(Long id) {
		return daoRepository.deleteById(id);
	}


	public List<String> findAllKeyWordContent() {
		List<String> collect = findAll().stream()
				.map(p -> p.getContent())
				.collect(Collectors.toList());
		return collect;
	}

	/**
	 * 添加关键字和修改关键字分别查询数据库是否存在重复数据
	 * @param keyWord 查询条件载体
	 * @return 存在数据库查询到的个数
	 * wanghuidong modify 2018-12-12 完善此方法
	 */
	public int getKeywordCount(KeyWord keyWord){
		int count;
		//添加和修改操作分别获取个数
		if(keyWord.getId()==null||keyWord.getId()<1){//添加
			count = daoRepository.getKeywordCountWithContent(keyWord);
		}else {//修改
			count =daoRepository.getKeywordCountWithId(keyWord);
		}
		return count;
	}
}
