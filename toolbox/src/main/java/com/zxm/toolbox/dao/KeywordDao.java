package com.zxm.toolbox.dao;

import java.util.List;

import com.zxm.toolbox.pojo.gt.Keyword;

public interface KeywordDao {
	
	List<Keyword> findAll();
	
	int deleteById(int id);

	int save(Keyword keyword);
	
	int update(Keyword keyword);

}
