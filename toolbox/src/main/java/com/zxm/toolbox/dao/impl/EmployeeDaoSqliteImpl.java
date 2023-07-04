package com.zxm.toolbox.dao.impl;

import com.zxm.toolbox.util.SqliteUtil;
import com.zxm.toolbox.dao.EmployeeDao;
import com.zxm.toolbox.pojo.attn.Employee;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class EmployeeDaoSqliteImpl implements EmployeeDao {

	private static final Logger LOGGER = LogManager.getLogger(EmployeeDaoSqliteImpl.class);
	private  static List<Employee> getBean(ResultSet rs) throws SQLException {
		List<Employee> list = new ArrayList<>();
		Employee bean;
		while ( rs.next() ) {
			bean = new Employee();
			bean.setId(rs.getString("id"));
			bean.setName(rs.getString("name"));
			bean.setCompanyId(rs.getInt("company_id"));
			bean.setCompany(rs.getString("company_name"));
			bean.setDepartmentId(rs.getInt("department_id"));
			bean.setDepartment(rs.getString("department_name"));
			bean.setPositionId(rs.getInt("position_id"));
			bean.setPosition(rs.getString("position_name"));
			bean.setGroupId(rs.getInt("attn_group_id"));
			bean.setGroup(rs.getString("attn_group_name"));
			bean.setIsOfficer(rs.getString("attn_officer"));
			list.add(bean);
		}
		return list;
	}

	@Override
	public List<Employee> findAll() {
		LOGGER.info("findAllEmployee");
		List<Employee> list;
		try {
			Statement stmt = SqliteUtil.getConnection().createStatement();
			ResultSet rs = stmt.executeQuery( "select * from employee;" );
			list = getBean(rs);
			return list;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public Employee findById(String id) {
		LOGGER.info("findEmployeeByID:" + id);
		List<Employee> list;
		try {
			PreparedStatement ps = SqliteUtil.getConnection()
					.prepareStatement("select * from employee where id=?;");
					ps.setString(1, id);
			ResultSet rs = ps.executeQuery();
			list = getBean(rs);
			if (list.isEmpty())
				return null;
			return list.get(0);
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public Employee findByName(String name) {
		LOGGER.info("findEmployeeByName:" + name);
		List<Employee> list;
		try {
			PreparedStatement ps = SqliteUtil.getConnection()
					.prepareStatement( "select * from employee where name=?;" );
			ps.setString(1, name);
			ResultSet rs = ps.executeQuery();
			list = getBean(rs);
			if(list.isEmpty())
				return null;
			return list.get(0);
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public Employee findAttnOfficer(String company, String group) {
		LOGGER.info("findAttnOfficer:" + company + "," + group);
		List<Employee> list;
		try {
			PreparedStatement ps = SqliteUtil.getConnection()
					.prepareStatement( "select * from employee where company_name = ? and attn_group_name = ? and attn_officer = 'Y';" );
			ps.setString(1, company);
			ps.setString(2, group);
			ResultSet rs = ps.executeQuery();
			list = getBean(rs);
			if(!list.isEmpty())
				return list.get(0);
			return null;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public int deleteById(String id) {
		PreparedStatement ps;
		try {
			ps = SqliteUtil.getConnection().prepareStatement("delete from employee where id= ?;");
			ps.setString(1, id);
			return ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			return -1;
		}
	}
	private void setProperties(Employee bean, PreparedStatement ps, int i) throws SQLException {
		ps.setString(i++,bean.getName());
		ps.setInt(i++,bean.getCompanyId());
		ps.setString(i++,bean.getCompany());
		ps.setInt(i++,bean.getDepartmentId());
		ps.setString(i++,bean.getDepartment());
		ps.setInt(i++,bean.getPositionId());
		ps.setString(i++,bean.getPosition());
		ps.setInt(i++,bean.getGroupId());
		ps.setString(i++,bean.getGroup());
		ps.setString(i,bean.getIsOfficer());
	}

	@Override
	public int save(Employee employee) {
		LOGGER.info(Thread.currentThread() .getStackTrace()[1].getMethodName());
		try {
			PreparedStatement ps = SqliteUtil.getConnection().prepareStatement(
					"insert into employee (id,name,company_id,company_name,department_id,department_name," +
							"position_id,position_name,attn_group_id,attn_group_name,attn_officer) " +
							"values (?,?,?,?,?,?,?,?,?,?,?);");
			ps.setString(1, employee.getId());
			setProperties(employee, ps, 2);
			return ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			return -1;
		}
	}

	@Override
	public int update(Employee bean) {
		LOGGER.info(Thread.currentThread() .getStackTrace()[1].getMethodName());
		try {
			PreparedStatement ps = SqliteUtil.getConnection().prepareStatement(
					"update employee set name=?,company_id=?,company_name=?,department_id=?,department_name=?" +
							",position_id=?,position_name=?,attn_group_id=?,attn_group_name=?,attn_officer=? where id = ?;");
			setProperties(bean, ps,1);
			ps.setString(11,bean.getId());
			return ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			return -1;
		}
	}

}
