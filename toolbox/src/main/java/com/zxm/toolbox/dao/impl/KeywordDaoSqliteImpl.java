package com.zxm.toolbox.dao.impl;

import com.zxm.toolbox.dao.KeywordDao;
import com.zxm.toolbox.pojo.gt.Keyword;
import com.zxm.toolbox.util.SqliteUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class KeywordDaoSqliteImpl implements KeywordDao {
	private static final Logger LOGGER = LogManager.getLogger(KeywordDaoSqliteImpl.class);
	private  static List<Keyword> getBean(ResultSet rs) throws SQLException {
		List<Keyword> list = new ArrayList<>();
		Keyword bean;
		while ( rs.next() ) {
			bean = new Keyword();
			bean.setId(rs.getInt("id"));
			bean.setKey(rs.getString("key"));
			bean.setFullName(rs.getString("full_name"));
			list.add(bean);
		}
		return list;
	}
	@Override
	public List<Keyword> findAll() {
		try {
			PreparedStatement ps = SqliteUtil.getConnection().prepareStatement(
					"select * from keyword;");
			ResultSet rs  = ps.executeQuery();
			return getBean(rs);
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public int deleteById(int id) {
		try {
			PreparedStatement ps = SqliteUtil.getConnection().prepareStatement(
						"delete from keyword where id = ?;");
			ps.setInt(1, id);
			return ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			return -1;
		}
	}

	@Override
	public int save(Keyword keyword) {
		try {
			PreparedStatement ps = SqliteUtil.getConnection().prepareStatement(
					"insert into keyword (key, full_name) values (?,?);");
			ps.setString(1, keyword.getKey());
			ps.setString(2, keyword.getFullName());
			return ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			return -1;
		}
	}

	@Override
	public int update(Keyword keyword) {
		try {
			PreparedStatement ps = SqliteUtil.getConnection().prepareStatement(
					"update keyword set key = ?, full_name = ? where id = ?;");
			ps.setString(1, keyword.getKey());
			ps.setString(2, keyword.getFullName());
			ps.setInt(3, keyword.getId());
			return ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			return -1;
		}
	}

}
