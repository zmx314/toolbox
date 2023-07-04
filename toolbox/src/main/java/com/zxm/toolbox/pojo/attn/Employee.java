package com.zxm.toolbox.pojo.attn;

import java.util.Objects;

/*
 * 
 */
public class Employee {
	private String id;
	private String name;
	private int companyId;
	private String company;
	private int departmentId;
	private String department;
	private int positionId;
	private String position;
	private int groupId;
	private String group;
	private String isOfficer;

	public Employee() {
		super();
	}

	public Employee(String name) {
		super();
		this.name = name;
	}

	public Employee(String name, Company company, Department department, Position position, Group group, String isOfficer) {
		this.name = name;
		this.companyId = company.getId();
		this.company = company.getName();
		this.departmentId = department.getId();
		this.department = department.getName();
		this.positionId = position.getId();
		this.position = position.getName();
		this.groupId = group.getId();
		this.group = group.getName();
		this.isOfficer = isOfficer;
	}

	public Employee(String id, String name, Company company, Department department, Position position, Group group, String isOfficer) {
		this.id = id;
		this.name = name;
		this.companyId = company.getId();
		this.company = company.getName();
		this.departmentId = department.getId();
		this.department = department.getName();
		this.positionId = position.getId();
		this.position = position.getName();
		this.groupId = group.getId();
		this.group = group.getName();
		this.isOfficer = isOfficer;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getCompanyId() {
		return companyId;
	}

	public void setCompanyId(int companyId) {
		this.companyId = companyId;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public int getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(int departmentId) {
		this.departmentId = departmentId;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public int getPositionId() {
		return positionId;
	}

	public void setPositionId(int positionId) {
		this.positionId = positionId;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public int getGroupId() {
		return groupId;
	}

	public void setGroupId(int groupId) {
		this.groupId = groupId;
	}

	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Employee employee = (Employee) o;
		if (id != employee.id) return false;
		if (!Objects.equals(name, employee.name)) return false;
		if (!Objects.equals(company, employee.company)) return false;
		if (!Objects.equals(department, employee.department)) return false;
		if (!Objects.equals(position, employee.position)) return false;
		return Objects.equals(group, employee.group);
	}

	public String getIsOfficer() {
		return isOfficer;
	}

	public void setIsOfficer(String isOfficer) {
		this.isOfficer = isOfficer;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, name, company, department, position, group);
	}

	@Override
	public String toString() {
		return "Employee{" +
				"id='" + id + '\'' +
				", name='" + name + '\'' +
				", companyId=" + companyId +
				", company='" + company + '\'' +
				", departmentId=" + departmentId +
				", department='" + department + '\'' +
				", positionId=" + positionId +
				", position='" + position + '\'' +
				", groupId=" + groupId +
				", group='" + group + '\'' +
				", isOfficer='" + isOfficer + '\'' +
				'}';
	}
}
