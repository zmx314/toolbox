package com.zxm.toolbox.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.zxm.toolbox.util.SqliteUtil;
import com.zxm.toolbox.pojo.attn.Section;

import com.zxm.toolbox.dao.SectionDao;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SectionDaoSqliteImpl implements SectionDao {
	private static final Logger LOGGER = LogManager.getLogger(SectionDaoSqliteImpl.class);

	@Override
	public List<Section> findAll() {
		LOGGER.info("findAll");
		List<Section> list = new ArrayList<>();
		try {
			Statement stmt = SqliteUtil.getConnection().createStatement();
			ResultSet rs = stmt.executeQuery( "select * from section;" );
			Section s;
			while ( rs.next() ) {
				int id = rs.getInt("id");
				String  name = rs.getString("name");
				s = new Section(id, name);
				list.add(s);
			}
			return list;
		} catch (SQLException e) {
			LOGGER.error(e.getStackTrace());
			return list;
		}

	}

	@Override
	public Section findById(int id) {
		Section s = new Section();
		try {
			Statement stmt = SqliteUtil.getConnection().createStatement();
			ResultSet rs = stmt.executeQuery( "select * from section where id=" + id + ";" );
			while ( rs.next() ) {
				String  name = rs.getString("name");
				s = new Section(id, name);
			}
			return s;
		} catch (SQLException e) {
			LOGGER.error(e.getStackTrace());
			return s;
		}
	}

	@Override
	public boolean add(Section s) {
		LOGGER.debug(Thread.currentThread().getStackTrace()[1].getMethodName());
		boolean flag = false;
		try {
			Statement stmt = SqliteUtil.getConnection().createStatement();
			flag = stmt.execute(
					"insert into section (name) values ('" + s.getName() + "');" );
			return flag;
		} catch (SQLException e) {
			LOGGER.error(e.getStackTrace());
			return flag;
		}

	}

	@Override
	public boolean delete(Section s) {
		LOGGER.info(Thread.currentThread().getStackTrace()[1].getMethodName());
		boolean flag = false;
		try {
			Statement stmt = SqliteUtil.getConnection().createStatement();
			if(s.getId() > 0) {
				flag = stmt.execute(
						"delete from section where id='" + s.getId() + "';");
			} else {
				flag = stmt.execute(
						"delete from section where name='" + s.getName() + "';");
			}
			return flag;
		} catch (SQLException e) {
			LOGGER.error(e.getStackTrace());
			return flag;
		}
	}

	@Override
	public int update(Section s) {
		LOGGER.info(Thread.currentThread().getStackTrace()[1].getMethodName());
		int i = -1;
		try {
			Statement stmt = SqliteUtil.getConnection().createStatement();
			i = stmt.executeUpdate(
					"update section set name ='" + s.getName() +
							"' where id='" + s.getId() + "';");
			return i;
		} catch (SQLException e) {
			LOGGER.error(e.getStackTrace());
			return i;
		}
	}
}
