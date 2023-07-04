package com.zxm.toolbox.resources;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Properties;

public class MyProperties {
	private static Logger LOGGER = LogManager.getLogger(MyProperties.class);
	private Properties props;
	private File propsFile;

	public MyProperties() {
		super();
	}
	
	public MyProperties(File propsFile) {
//		LOGGER.info(propsFile.getAbsolutePath());
		this.props = new Properties();
		this.propsFile = propsFile;
		InputStreamReader inStream;
		try {
			inStream = new InputStreamReader(new FileInputStream(propsFile), "UTF-8");
			props.load(inStream);
			inStream.close();
		} catch (FileNotFoundException e1) {
			LOGGER.error(e1.getMessage());
		} catch (IOException e) {
			LOGGER.error(e.getMessage());
		}
	}
	
	public Properties getProps() {
		return props;
	}

	public void setProps(Properties props) {
		this.props = props;
	}

	public String getValue(String key) {
		return props.getProperty(key);
	}

	/**
	 * 修改或添加键值对 如果key存在，修改, 反之，添加。
	 * 
	 * @param key   键
	 * @param value 键对应的值
	 */
	public void setValue(String key, String value) {
		OutputStreamWriter outStream;
		try {
			outStream = new OutputStreamWriter(new FileOutputStream(propsFile), "UTF-8");
			props.setProperty(key, value);
			props.store(outStream, "Update '" + key + "' value");
			outStream.close();
		} catch (FileNotFoundException e1) {
			System.out.println("配置文件丢失");
			LOGGER.error("配置文件丢失");
		} catch (IOException e) {
			System.out.println("加载设置文件时发生输入输出异常");
			LOGGER.error("加载设置文件时发生输入输出异常");
		}
	}
}
