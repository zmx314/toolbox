package com.zxm.toolbox.dao.impl;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.zxm.toolbox.util.SqliteUtil;
import com.zxm.toolbox.pojo.attn.Company;
import com.zxm.toolbox.dao.CompanyDao;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CompanyDaoSqliteImpl implements CompanyDao {
	private static final Logger LOGGER = LogManager.getLogger(CompanyDaoSqliteImpl.class);

	@Override
	public List<Company> findAll() {
		LOGGER.info("findAll");
		List<Company> list = new ArrayList<>();
		try {
			Statement stmt = SqliteUtil.getConnection().createStatement();
			ResultSet rs = stmt.executeQuery( "select * from company;");
			Company c;
			while ( rs.next() ) {
				int id = rs.getInt("id");
				String  name = rs.getString("name");
				c = new Company(id, name);
				list.add(c);
			}
			return list;
		} catch (SQLException e) {
			LOGGER.error(e.getStackTrace());
			return list;
		}

	}

	@Override
	public Company findById(int id) {
		Company c = new Company();
		try {
			PreparedStatement ps = SqliteUtil.getConnection().prepareStatement(
					"select * from company where id= ?;");
			ps.setInt(1, id);
			ResultSet rs = ps.executeQuery();
			while ( rs.next() ) {
				String  name = rs.getString("name");
				c = new Company(id, name);
			}
			return c;
		} catch (SQLException e) {
			e.printStackTrace();
			return c;
		}
	}

	@Override
	public int save(Company company) {
		LOGGER.debug(Thread.currentThread().getStackTrace()[1].getMethodName());
		int flag = 0;
		PreparedStatement ps;
		try {
			ps = SqliteUtil.getConnection().prepareStatement(
					"insert into company (name) values (?);"
			);
			flag = ps.executeUpdate();
			return flag;
		} catch (SQLException e) {
			e.printStackTrace();
			return flag;
		}

	}

	@Override
	public int deleteById(int id) {
		LOGGER.info(Thread.currentThread().getStackTrace()[1].getMethodName());
		int flag;
		PreparedStatement ps;
		try {
				ps = SqliteUtil.getConnection().prepareStatement(
						"delete from company where id = ?;");
				ps.setInt(1, id);
				flag = ps.executeUpdate();
			return flag;
		} catch (SQLException e) {
			LOGGER.error(e.getStackTrace());
			return 0;
		}
	}

	@Override
	public int update(Company company) {
		LOGGER.info(Thread.currentThread().getStackTrace()[1].getMethodName());
		int i = -1;
		try {
			Statement stmt = SqliteUtil.getConnection().createStatement();
			i = stmt.executeUpdate(
					"update company set name ='" + company.getName() +
							"' where id='" + company.getId() + "';");
			return i;
		} catch (SQLException e) {
			LOGGER.error(e.getStackTrace());
			return i;
		}

	}

}
