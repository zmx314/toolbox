package com.zxm.toolbox.dao.impl;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.zxm.toolbox.util.SqliteUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.util.CellAddress;

import com.zxm.toolbox.dao.GroupDao;
import com.zxm.toolbox.pojo.attn.Group;

public class GroupDaoSqliteImpl implements GroupDao {

	private static final Logger LOGGER = LogManager.getLogger(GroupDaoSqliteImpl.class);

	private  static List<Group> getBean(ResultSet rs) throws SQLException {
		List<Group> list = new ArrayList<>();
		Group bean;
		while ( rs.next() ) {
			bean = new Group();
			bean.setId(rs.getInt("id"));
			bean.setName(rs.getString("name"));
			bean.setDateCell(new CellAddress(rs.getString("date_cell")));
			bean.setStartCell(new CellAddress(rs.getString("start_cell")));
			bean.setDutyCell(new CellAddress(rs.getString("duty_cell")));
			bean.setTimeCell(new CellAddress(rs.getString("time_cell")));
			bean.setManHourCell(new CellAddress(rs.getString("man_hour_cell")));
			bean.setOffCell(new CellAddress(rs.getString("off_cell")));
			list.add(bean);
		}
		return list;
	}
	@Override
	public List<Group> findAll() {
		LOGGER.info(Thread.currentThread() .getStackTrace()[1].getMethodName());
		List<Group> list;
		try {
			Statement stmt = SqliteUtil.getConnection().createStatement();
			ResultSet rs = stmt.executeQuery( "select * from attn_group;" );
			list = getBean(rs);
			return list;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	@Override
	public Group findByName(String name) {
		LOGGER.info(Thread.currentThread() .getStackTrace()[1].getMethodName());
		List<Group> list;
		try {
			Statement stmt = SqliteUtil.getConnection().createStatement();
			ResultSet rs = stmt.executeQuery( "select * from attn_group where name = '" + name + "';" );
			list = getBean(rs);
			return list.get(0);
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public Group findById(int id) {
		LOGGER.info(Thread.currentThread() .getStackTrace()[1].getMethodName());
		List<Group> list;
		try {
			Statement stmt = SqliteUtil.getConnection().createStatement();
			ResultSet rs = stmt.executeQuery( "select * from attn_group where id=" + id + ";" );
			list = getBean(rs);
			if (!list.isEmpty())
				return list.get(0);
			else
				return null;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public boolean delete(Group group) {
		LOGGER.info(Thread.currentThread() .getStackTrace()[1].getMethodName());
		boolean flag;
		try {
			Statement stmt = SqliteUtil.getConnection().createStatement();
			if(group.getId() > 0) {
				flag = stmt.execute(
						"delete from attn_group where id='" + group.getId() + "';");
			} else {
				flag = stmt.execute(
						"delete from attn_group where name='" + group.getName() + "';");
			}
			return flag;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public int save(Group group) {
		LOGGER.info(Thread.currentThread() .getStackTrace()[1].getMethodName());
		int flag = -1;
		try {
			PreparedStatement ps = SqliteUtil.getConnection().prepareStatement(
					"insert into attn_group (name,date_cell,start_cell,duty_cell,time_cell,man_hour_cell,off_cell) " +
							"values (?,?,?,?,?,?,?);");
			setProperties(group, ps);
			flag = ps.executeUpdate();
			return flag;
		} catch (SQLException e) {
			e.printStackTrace();
			return flag;
		}
	}

	private void setProperties(Group group, PreparedStatement ps) throws SQLException {
		ps.setString(1,group.getName());
		ps.setString(2,group.getDateCell().toString());
		ps.setString(3,group.getStartCell().toString());
		ps.setString(4,group.getDutyCell().toString());
		ps.setString(5,group.getTimeCell().toString());
		ps.setString(6,group.getManHourCell().toString());
		ps.setString(7,group.getOffCell().toString());
	}

	@Override
	public int update(Group group) {
		LOGGER.info(Thread.currentThread() .getStackTrace()[1].getMethodName());
		int flag = -1;
		try {
			PreparedStatement ps = SqliteUtil.getConnection().prepareStatement(
					"update attn_group set name=?,date_cell=?,start_cell=?,duty_cell=?,time_cell=?,man_hour_cell=?,off_cell=?" +
							"where id = ?;");
			setProperties(group, ps);
			ps.setInt(8,group.getId());
			flag = ps.executeUpdate();
			return flag;
		} catch (SQLException e) {
			e.printStackTrace();
			return flag;
		}
	}

}