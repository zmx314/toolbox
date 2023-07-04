package com.zxm.toolbox.dao;

import java.util.List;

import com.zxm.toolbox.pojo.attn.Group;

public interface GroupDao {
	List<Group> findAll();
	Group findByName(String groupName);
	Group findById(int id);
	boolean delete(Group group);
	int save(Group group);
	int update(Group group);
}
