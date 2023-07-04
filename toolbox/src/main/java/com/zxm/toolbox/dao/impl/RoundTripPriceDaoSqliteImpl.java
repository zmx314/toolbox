package com.zxm.toolbox.dao.impl;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.zxm.toolbox.util.SqliteUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.zxm.toolbox.dao.RoundTripPriceDao;
import com.zxm.toolbox.pojo.gt.RoundTripPrice;

public class RoundTripPriceDaoSqliteImpl implements RoundTripPriceDao {
	private static final Logger LOGGER = LogManager.getLogger(RoundTripPriceDaoSqliteImpl.class);
	private  static List<RoundTripPrice> getBean(ResultSet rs) throws SQLException {
		List<RoundTripPrice> list = new ArrayList<>();
		RoundTripPrice bean;
		while ( rs.next() ) {
			bean = new RoundTripPrice();
			bean.setId(rs.getInt("id"));
			bean.setShippingCompany(rs.getString("shipping_company_name"));
			bean.setTicketType(rs.getString("ticket_type"));
			bean.setStartDate(LocalDate.parse(rs.getString("start_date")));
			bean.setEndDate(LocalDate.parse(rs.getString("end_date")));
			bean.setPrice(new BigDecimal(rs.getString("price")));
			list.add(bean);
		}
		return list;
	}
	@Override
	public List<RoundTripPrice> findAll() {
		LOGGER.debug(Thread.currentThread().getStackTrace()[1].getMethodName());
		try {
			PreparedStatement ps = SqliteUtil.getConnection().prepareStatement(
					"select * from round_trip_price;");
			ResultSet rs = ps.executeQuery();
			return getBean(rs);
		} catch (SQLException e) {
			e.printStackTrace();
			return new ArrayList<>();
		}
	}

	@Override
	public RoundTripPrice findById(int id) {
		List<RoundTripPrice> list;
		try {
			PreparedStatement ps = SqliteUtil.getConnection().prepareStatement(
					"select * from round_trip_price where id=?;");
			ps.setInt(1, id);
			ResultSet rs = ps.executeQuery();
			list = getBean(rs);
			return list.isEmpty() ? null : list.get(0);
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public List<RoundTripPrice> findByShippingCompanyName(String shippingCompanyName) {
		LOGGER.debug(Thread.currentThread().getStackTrace()[1].getMethodName());
		try {
			PreparedStatement ps = SqliteUtil.getConnection().prepareStatement(
					"select * from round_trip_price where shipping_company_name=?;");
			ps.setString(1, shippingCompanyName);
			ResultSet rs = ps.executeQuery();
			return getBean(rs);
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
					"delete from round_trip_price where id=?;");
			ps.setInt(1, id);
			return ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			return -1;
		}
	}

	private void setProperties(RoundTripPrice roundTripPrice, PreparedStatement ps, int index) throws SQLException {
		int i = index;
		ps.setString(i++, roundTripPrice.getShippingCompany());
		ps.setString(i++, roundTripPrice.getTicketType());
		ps.setString(i++, roundTripPrice.getStartDate().toString());
		ps.setString(i++, roundTripPrice.getEndDate().toString());
		ps.setString(i, roundTripPrice.getPrice().toString());
	}

	@Override
	public int save(RoundTripPrice obj) {
		LOGGER.debug(Thread.currentThread().getStackTrace()[1].getMethodName());
		try {
			PreparedStatement ps = SqliteUtil.getConnection().prepareStatement(
					"insert into round_trip_price (shipping_company_name,ticket_type,start_date,end_date,price) values (?,?,?,?,?);");
			setProperties(obj, ps, 1);
			return ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			return -1;
		}
	}

	@Override
	public int update(RoundTripPrice obj) {
		LOGGER.debug(Thread.currentThread().getStackTrace()[1].getMethodName());
		try {
			PreparedStatement ps = SqliteUtil.getConnection().prepareStatement(
					"update round_trip_price set shipping_company_name=?,ticket_type=?,start_date=?,end_date=?,price=? where id = ?;");
			setProperties(obj, ps, 1);
			ps.setInt(6, obj.getId());
			return ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			return -1;
		}
	}
}
