package com.zxm.toolbox.dao;

import com.zxm.toolbox.pojo.attn.Department;

import java.util.List;

public interface DepartmentDao {

	List<Department> findAll();

	Department findById(int id);

	boolean add(Department dept);

	boolean delete(Department dept);

	int update(Department dept);

}
