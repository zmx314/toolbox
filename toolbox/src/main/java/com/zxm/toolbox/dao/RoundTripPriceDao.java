package com.zxm.toolbox.dao;

import java.util.List;

import com.zxm.toolbox.pojo.gt.RoundTripPrice;

public interface RoundTripPriceDao {
	List<RoundTripPrice> findAll();

	RoundTripPrice findById(int id);

	List<RoundTripPrice> findByShippingCompanyName(String shippingCompanyName);

	int deleteById(int id);

	int save(RoundTripPrice obj);

	int update(RoundTripPrice obj);
	
}
