package com.zxm.toolbox.dao;

import com.zxm.toolbox.pojo.attn.Section;

import java.util.List;

public interface SectionDao {

	List<Section> findAll();

	Section findById(int id);

	boolean add(Section s);

	boolean delete(Section s);

	int update(Section s);
}
