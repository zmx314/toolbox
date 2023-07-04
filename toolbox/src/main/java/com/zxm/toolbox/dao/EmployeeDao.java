package com.zxm.toolbox.dao;

import java.util.List;

import com.zxm.toolbox.pojo.attn.Employee;

public interface EmployeeDao {
	List<Employee> findAll();

	Employee findById(String id);

	Employee findByName(String name);

	Employee findAttnOfficer(String company, String group);

	int deleteById(String id);

	int save(Employee employee);

	int update(Employee employee);
}
