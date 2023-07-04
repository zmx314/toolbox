package com.zxm.toolbox.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.zxm.toolbox.util.SqliteUtil;
import com.zxm.toolbox.pojo.attn.Department;

import com.zxm.toolbox.dao.DepartmentDao;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DepartmentDaoSqliteImpl implements DepartmentDao {
	private static final Logger LOGGER = LogManager.getLogger(DepartmentDaoSqliteImpl.class);

	@Override
	public List<Department> findAll() {
		LOGGER.info("findAll");
		List<Department> list = new ArrayList<>();
		try {
			Statement stmt = SqliteUtil.getConnection().createStatement();
			ResultSet rs = stmt.executeQuery( "select * from department;" );
			Department d;
			while ( rs.next() ) {
				int id = rs.getInt("id");
				String  name = rs.getString("name");
				d = new Department(id, name);
				list.add(d);
			}
			return list;
		} catch (SQLException e) {
			LOGGER.error(e.getStackTrace());
			return list;
		}

	}

	@Override
	public Department findById(int id) {
		Department d = new Department();
		try {
			Statement stmt = SqliteUtil.getConnection().createStatement();
			ResultSet rs = stmt.executeQuery( "select * from department where id=" + id + ";" );
			while ( rs.next() ) {
				String  name = rs.getString("name");
				d = new Department(id, name);
			}
			return d;
		} catch (SQLException e) {
			return d;
		}
	}

	@Override
	public boolean add(Department dept) {
		LOGGER.debug(Thread.currentThread().getStackTrace()[1].getMethodName());
		boolean flag;
		try {
			Statement stmt = SqliteUtil.getConnection().createStatement();
			flag = stmt.execute(
					"insert into department (name) values ('" + dept.getName() + "');" );
			return flag;
		} catch (SQLException e) {
			LOGGER.error(e.getStackTrace());
			return false;
		}

	}

	@Override
	public boolean delete(Department dept) {
		LOGGER.info(Thread.currentThread().getStackTrace()[1].getMethodName());
		boolean flag;
		try {
			Statement stmt = SqliteUtil.getConnection().createStatement();
			if(dept.getId() > 0) {
				flag = stmt.execute(
						"delete from department where id='" + dept.getId() + "';");
			} else {
				flag = stmt.execute(
						"delete from department where name='" + dept.getName() + "';");
			}
			return flag;
		} catch (SQLException e) {
			LOGGER.error(e.getStackTrace());
			return false;
		}
	}

	@Override
	public int update(Department dept) {
		LOGGER.info(Thread.currentThread().getStackTrace()[1].getMethodName());
		int i = -1;
		try {
			Statement stmt = SqliteUtil.getConnection().createStatement();
			i = stmt.executeUpdate(
					"update department set name ='" + dept.getName() +
							"' where id='" + dept.getId() + "';");
			return i;
		} catch (SQLException e) {
			LOGGER.error(e.getStackTrace());
			return i;
		}

	}

}
