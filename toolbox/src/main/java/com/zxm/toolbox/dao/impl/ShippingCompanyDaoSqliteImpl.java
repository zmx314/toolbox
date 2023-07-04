package com.zxm.toolbox.dao.impl;

import com.zxm.toolbox.dao.ShippingCompanyDao;
import com.zxm.toolbox.pojo.gt.ShippingCompany;
import com.zxm.toolbox.util.SqliteUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ShippingCompanyDaoSqliteImpl implements ShippingCompanyDao {
	private static Logger LOGGER = LogManager.getLogger(ShippingCompanyDaoSqliteImpl.class);
	private  static List<ShippingCompany> getBean(ResultSet rs) throws SQLException {
		List<ShippingCompany> list = new ArrayList<>();
		ShippingCompany bean;
		while ( rs.next() ) {
			bean = new ShippingCompany();
			bean.setId(rs.getInt("id"));
			bean.setName(rs.getString("name"));
			bean.setGroupId(rs.getInt("group_id"));
			bean.setGroupName(rs.getString("group_name"));
			bean.setType(rs.getString("type"));
			list.add(bean);
		}
		return list;
	}
	@Override
	public List<ShippingCompany> findAll() {
		LOGGER.debug(Thread.currentThread().getStackTrace()[1].getMethodName());
		try {
			PreparedStatement ps = SqliteUtil.getConnection().prepareStatement("select * from shipping_company;" );
			ResultSet rs = ps.executeQuery( );
			return getBean(rs);
		} catch (SQLException e) {
			e.printStackTrace();
			return new ArrayList<>();
		}
	}

	@Override
	public List<ShippingCompany> findByGroupName(String groupName) {
		LOGGER.debug(Thread.currentThread().getStackTrace()[1].getMethodName());
		try {
			PreparedStatement ps = SqliteUtil.getConnection().prepareStatement(
					"select * from shipping_company where group_name=?;" );
			ps.setString(1,groupName);
			ResultSet rs = ps.executeQuery( );
			return getBean(rs);
		} catch (SQLException e) {
			e.printStackTrace();
			return new ArrayList<>();
		}
	}

	@Override
	public ShippingCompany findByName(String name) {
		LOGGER.debug(Thread.currentThread().getStackTrace()[1].getMethodName());
		List<ShippingCompany> list;
		try {
			PreparedStatement ps = SqliteUtil.getConnection().prepareStatement(
					"select * from shipping_company where name=?;" );
			ps.setString(1, name);
			ResultSet rs = ps.executeQuery( );
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
	public int deleteById(int id) {
		LOGGER.debug(Thread.currentThread().getStackTrace()[1].getMethodName());
		try {
			PreparedStatement ps = SqliteUtil.getConnection().prepareStatement(
					"delete from shipping_company where id = ?;" );
			ps.setInt(1, id);
			return ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			return -1;
		}
	}

	private void setProperties(ShippingCompany bean, PreparedStatement ps, int i) throws SQLException {
		ps.setString(i++,bean.getName());
		ps.setInt(i++,bean.getGroupId());
		ps.setString(i++,bean.getGroupName());
		ps.setString(i,bean.getType());
	}

	@Override
	public int save(ShippingCompany obj) {
		LOGGER.debug(Thread.currentThread().getStackTrace()[1].getMethodName());
		try {
			PreparedStatement ps = SqliteUtil.getConnection().prepareStatement(
					"insert into shipping_company (name,group_id,group_name,type) values (?,?,?,?);");
			setProperties(obj, ps, 1);
			return ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			return -1;
		}
	}

	@Override
	public int update(ShippingCompany obj) {
		LOGGER.debug(Thread.currentThread().getStackTrace()[1].getMethodName());
		try {
			PreparedStatement ps = SqliteUtil.getConnection().prepareStatement(
					"update shipping_company set name=?,group_id=?,group_name=?,type=? where id=?;");
			setProperties(obj, ps, 1);
			ps.setInt(5, obj.getId());
			return ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			return -1;
		}
	}

}
