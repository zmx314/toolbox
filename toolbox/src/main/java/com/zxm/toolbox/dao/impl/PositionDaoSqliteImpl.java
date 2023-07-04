package com.zxm.toolbox.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.zxm.toolbox.util.SqliteUtil;
import com.zxm.toolbox.pojo.attn.Position;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.zxm.toolbox.dao.PositionDao;

public class PositionDaoSqliteImpl implements PositionDao {
	private static final Logger LOGGER = LogManager.getLogger(PositionDaoSqliteImpl.class);

	@Override
	public List<Position> findAll() {
		LOGGER.info("findAll");
		List<Position> list = new ArrayList<>();
		try {
			Statement stmt = SqliteUtil.getConnection().createStatement();
			ResultSet rs = stmt.executeQuery( "select * from position;" );
			Position p;
			while ( rs.next() ) {
				int id = rs.getInt("id");
				String  name = rs.getString("name");
				p = new Position(id, name);
				list.add(p);
			}
			return list;
		} catch (SQLException e) {
			LOGGER.error(e.getStackTrace());
			return list;
		}

	}

	@Override
	public Position findById(int id) {
		Position p = new Position();
		try {
			Statement stmt = SqliteUtil.getConnection().createStatement();
			ResultSet rs = stmt.executeQuery( "select * from position where id=" + id + ";" );
			while ( rs.next() ) {
				String  name = rs.getString("name");
				p = new Position(id, name);
			}
			return p;
		} catch (SQLException e) {
			LOGGER.error(e.getStackTrace());
			return p;
		}
	}

	@Override
	public boolean add(Position p) {
		LOGGER.debug(Thread.currentThread().getStackTrace()[1].getMethodName());
		boolean flag = false;
		try {
			Statement stmt = SqliteUtil.getConnection().createStatement();
			flag = stmt.execute(
					"insert into position (name) values ('" + p.getName() + "');" );
			return flag;
		} catch (SQLException e) {
			LOGGER.error(e.getStackTrace());
			return flag;
		}

	}

	@Override
	public boolean delete(Position p) {
		LOGGER.info(Thread.currentThread().getStackTrace()[1].getMethodName());
		boolean flag = false;
		try {
			Statement stmt = SqliteUtil.getConnection().createStatement();
			if(p.getId() > 0) {
				flag = stmt.execute(
						"delete from position where id='" + p.getId() + "';");
			} else {
				flag = stmt.execute(
						"delete from position where name='" + p.getName() + "';");
			}
			return flag;
		} catch (SQLException e) {
			LOGGER.error(e.getStackTrace());
			return flag;
		}
	}

	@Override
	public int update(Position p) {
		LOGGER.info(Thread.currentThread().getStackTrace()[1].getMethodName());
		int i = -1;
		try {
			Statement stmt = SqliteUtil.getConnection().createStatement();
			i = stmt.executeUpdate(
					"update position set name ='" + p.getName() +
							"' where id='" + p.getId() + "';");
			return i;
		} catch (SQLException e) {
			LOGGER.error(e.getStackTrace());
			return i;
		}
	}
}
