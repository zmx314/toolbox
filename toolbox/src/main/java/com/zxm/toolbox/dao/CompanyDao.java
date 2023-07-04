package com.zxm.toolbox.dao;

import com.zxm.toolbox.pojo.attn.Company;

import java.util.List;

public interface CompanyDao {
	
	List<Company> findAll();

	Company findById(int id);

	int save(Company company);
	
	int deleteById(int id);

	int update(Company company);

}
