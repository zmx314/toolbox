package com.zxm.toolbox.dao;

import java.util.List;

import com.zxm.toolbox.pojo.gt.ShippingGroup;

public interface ShippingGroupDao {
	List<ShippingGroup> findAll();
	
	ShippingGroup findByName(String name);

	ShippingGroup findById(int id);
	
	int deleteByName(String name);

	int deleteById(int id);

	int save(ShippingGroup obj);

	int update(ShippingGroup obj);
	
}
