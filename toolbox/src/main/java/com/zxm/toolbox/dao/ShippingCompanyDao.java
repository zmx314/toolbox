package com.zxm.toolbox.dao;

import java.util.List;

import com.zxm.toolbox.pojo.gt.ShippingCompany;

public interface ShippingCompanyDao {
	List<ShippingCompany> findAll();
	
	ShippingCompany findByName(String name);

	List<ShippingCompany> findByGroupName(String groupName);
	
	int deleteById(int id);

	int save(ShippingCompany obj);

	int update(ShippingCompany obj);
	
}
