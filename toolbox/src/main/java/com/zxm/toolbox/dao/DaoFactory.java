package com.zxm.toolbox.dao;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.util.Objects;
import java.util.Properties;

public class DaoFactory {
	private static final DaoFactory instance = new DaoFactory();
	private CompanyDao companyDao = null;
	private DepartmentDao departmentDao = null;
	private SectionDao sectionDao = null;
	private PositionDao positionDao = null;
	private GroupDao groupDao = null;
	private EmployeeDao employeeDao = null;
	private AttnOfficerDao attnOfficerDao = null;
	private ShippingCompanyDao shippingCompanyDao = null;
	private ShippingGroupDao shippingGroupDao = null;
	private RoundTripPriceDao roundTripPriceDao = null;
	private KeywordDao keywordDao = null;

	private DaoFactory() {
		try {
			Properties properties = new Properties();
			InputStreamReader inStream = new InputStreamReader(
					Objects.requireNonNull(DaoFactory.class.getResourceAsStream(
							"dao.properties")), "UTF-8");
			properties.load(inStream);
			inStream.close();
			String daoClass = properties.getProperty("CompanyDaoClass");
			companyDao = (CompanyDao)Class.forName(daoClass).getDeclaredConstructor().newInstance();
			daoClass = properties.getProperty("DepartmentDaoClass");
			departmentDao = (DepartmentDao)Class.forName(daoClass).getDeclaredConstructor().newInstance();
			daoClass = properties.getProperty("SectionDaoClass");
			sectionDao = (SectionDao)Class.forName(daoClass).getDeclaredConstructor().newInstance();
			daoClass = properties.getProperty("PositionDaoClass");
			positionDao = (PositionDao)Class.forName(daoClass).getDeclaredConstructor().newInstance();
			daoClass = properties.getProperty("GroupDaoClass");
			groupDao = (GroupDao)Class.forName(daoClass).getDeclaredConstructor().newInstance();
			daoClass = properties.getProperty("EmployeeDaoClass");
			employeeDao = (EmployeeDao)Class.forName(daoClass).getDeclaredConstructor().newInstance();
			daoClass = properties.getProperty("AttnOfficerDaoClass");
			attnOfficerDao = (AttnOfficerDao)Class.forName(daoClass).getDeclaredConstructor().newInstance();
			daoClass = properties.getProperty("ShippingCompanyDaoClass");
			shippingCompanyDao = (ShippingCompanyDao)Class.forName(daoClass).getDeclaredConstructor().newInstance();
			daoClass = properties.getProperty("ShippingGroupDaoClass");
			shippingGroupDao = (ShippingGroupDao)Class.forName(daoClass).getDeclaredConstructor().newInstance();
			daoClass = properties.getProperty("RoundTripPriceDaoClass");
			roundTripPriceDao = (RoundTripPriceDao)Class.forName(daoClass).getDeclaredConstructor().newInstance();
			daoClass = properties.getProperty("KeywordDaoClass");
			keywordDao = (KeywordDao)Class.forName(daoClass).getDeclaredConstructor().newInstance();
		} catch (FileNotFoundException e1) {
			System.out.println("配置文件丢失");
		} catch (IOException e) {
			System.out.println("输出入输出异常！");
		} catch (ClassNotFoundException | InvocationTargetException | InstantiationException | IllegalAccessException | NoSuchMethodException e) {
			e.printStackTrace();
		}
	}

	public static DaoFactory  getInstance() {
		return instance;
	}

	public CompanyDao getCompanyDao() {
		return companyDao;
	}

	public DepartmentDao getDepartmentDao() {
		return departmentDao;
	}

	public SectionDao getSectionDao() {
		return sectionDao;
	}

	public PositionDao getPositionDao() {
		return positionDao;
	}

	public GroupDao getGroupDao() {
		return groupDao;
	}

	public EmployeeDao getEmployeeDao() {
		return employeeDao;
	}

	public AttnOfficerDao getAttnOfficerDao() {
		return attnOfficerDao;
	}

	public ShippingCompanyDao getShippingCompanyDao() {
		return shippingCompanyDao;
	}

	public ShippingGroupDao getShippingGroupDao() {
		return shippingGroupDao;
	}

	public RoundTripPriceDao getRoundTripPriceDao() {
		return roundTripPriceDao;
	}

	public KeywordDao getKeywordDao() {
		return keywordDao;
	}

}
