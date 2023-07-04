package com.zxm.toolbox.dao;

import com.zxm.toolbox.pojo.attn.AttnOfficer;

import java.util.List;

public interface AttnOfficerDao {
	List<AttnOfficer> findAll();

	AttnOfficer find(String companyName, String groupName);

	boolean delete(String groupName, String companyName);

	boolean save(AttnOfficer officer);

	boolean update(AttnOfficer officer);
}
