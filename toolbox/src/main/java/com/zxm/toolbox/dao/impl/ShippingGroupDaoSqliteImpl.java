package com.zxm.toolbox.dao.impl;

import com.zxm.toolbox.dao.ShippingGroupDao;
import com.zxm.toolbox.pojo.gt.ShippingGroup;
import com.zxm.toolbox.util.SqliteUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ShippingGroupDaoSqliteImpl implements ShippingGroupDao {
	private static final Logger LOGGER = LogManager.getLogger(ShippingGroupDaoSqliteImpl.class);
	private  static List<ShippingGroup> getBean(ResultSet rs) throws SQLException {
		List<ShippingGroup> list = new ArrayList<>();
		ShippingGroup bean;
		while ( rs.next() ) {
			bean = new ShippingGroup();
			bean.setId(rs.getInt("id"));
			bean.setName(rs.getString("name"));
			bean.setType(rs.getString("type"));
			bean.setIsCompanySeparate(rs.getString("company_separate"));
			list.add(bean);
		}
		return list;
	}
	@Override
	public List<ShippingGroup> findAll() {
		try {
			PreparedStatement ps = SqliteUtil.getConnection().prepareStatement("select * from shipping_group;" );
			ResultSet rs = ps.executeQuery( );
			return getBean(rs);
		} catch (SQLException e) {
			e.printStackTrace();
			return new ArrayList<>();
		}
	}

	@Override
	public ShippingGroup findByName(String name) {
		List<ShippingGroup> list;
		try {
			PreparedStatement ps = SqliteUtil.getConnection().prepareStatement(
					"select * from shipping_group where name = ?;" );
			ps.setString(1, name);
			ResultSet rs = ps.executeQuery( );
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
	public ShippingGroup findById(int id) {
		List<ShippingGroup> list;
		try {
			PreparedStatement ps = SqliteUtil.getConnection().prepareStatement(
					"select * from shipping_group where id = ?;" );
			ps.setInt(1, id);
			ResultSet rs = ps.executeQuery( );
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
	public int deleteByName(String name) {
		try {
			PreparedStatement ps = SqliteUtil.getConnection().prepareStatement(
					"delete from shipping_group where name = ?;" );
			ps.setString(1, name);
			return ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			return -1;
		}
	}

	@Override
	public int deleteById(int id) {
		try {
			PreparedStatement ps = SqliteUtil.getConnection().prepareStatement(
					"delete from shipping_group where id = ?;" );
			ps.setInt(1, id);
			return ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			return -1;
		}
	}

	private void setProperties(ShippingGroup group, PreparedStatement ps, int i) throws SQLException {
		ps.setString(i++,group.getName());
		ps.setString(i++,group.getType());
		ps.setString(i,group.getIsCompanySeparate());
	}

	@Override
	public int save(ShippingGroup obj) {
		try {
			PreparedStatement ps = SqliteUtil.getConnection().prepareStatement(
					"insert into shipping_group (name,type,company_separate) values (?,?,?);");
			setProperties(obj, ps, 1);
			return ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			return -1;
		}
	}

	@Override
	public int update(ShippingGroup obj) {
		try {
			PreparedStatement ps = SqliteUtil.getConnection().prepareStatement(
					"update shipping_group set name = ?, type = ?, company_separate = ? where id = ?;");
			setProperties(obj, ps, 1);
			ps.setInt(4, obj.getId());
			return ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			return -1;
		}
	}

}
