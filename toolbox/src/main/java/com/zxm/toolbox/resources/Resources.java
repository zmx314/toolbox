package com.zxm.toolbox.resources;

import com.zxm.toolbox.util.XMLUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

public class Resources {

    private static final Logger LOGGER = LogManager.getLogger(Resources.class);
	public static String JAR_PATH;
	public static String HOME_DIR;
	public static File DB_PROP_FILE = null;
	public static File XML_DATA_FILE = null;
	public static File SQLITE_DATA_FILE = null;
	public static File SETTINGS_PROPS_FILE = null;
	public static File TEMPLATE_PROPS_FILE = null;
	public static File DUTY_ROSTER_PROPS = null;
	public static File UI_PROPS_FILE = null;
	static {
		JAR_PATH = URLDecoder.decode(XMLUtil.class.getProtectionDomain().getCodeSource().getLocation().getPath(),
				StandardCharsets.UTF_8);
		LOGGER.info("JAR_PATH:" + JAR_PATH);
		HOME_DIR = new File(JAR_PATH).getParent();
		LOGGER.info("HOME_DIRECTORY:" + HOME_DIR);
	}

	public static boolean init() {
		DB_PROP_FILE = new File(HOME_DIR + "\\conf\\db.properties");
		if (!DB_PROP_FILE.exists()) {
			try {
				DB_PROP_FILE.createNewFile();
				LOGGER.info("加载数据库配置文件成功");
			} catch (IOException e) {
				LOGGER.error("加载数据库配置文件失败");
				return false;
			}
		}
		XML_DATA_FILE = new File(HOME_DIR + "\\data\\data.xml");
		LOGGER.info("XML_DATA_FILE:" + XML_DATA_FILE);
		if (XML_DATA_FILE.exists()) {
			LOGGER.info("加载XML数据文件成功");
		} else {
			LOGGER.error("XML数据文件丢失");
			return false;
		}
		SQLITE_DATA_FILE = new File(HOME_DIR + "\\data\\toolbox.db");
		LOGGER.info("SQLITE_DATA_FILE:" + SQLITE_DATA_FILE);
		if (XML_DATA_FILE.exists()) {
			LOGGER.info("加载本地数据库文件成功");
		} else {
			LOGGER.error("加载本地数据库文件失败");
			return false;
		}
		DUTY_ROSTER_PROPS = new File(HOME_DIR + "\\conf\\dutyRoster.properties");
		LOGGER.info("SETTINGS_PROPS_FILE:" + DUTY_ROSTER_PROPS);
		if (DUTY_ROSTER_PROPS.exists()) {
			LOGGER.info("加载考勤表偏移值配置文件成功");
		} else {
			LOGGER.error("加载考勤表偏移值配置文件失败");
			return false;
		}
		SETTINGS_PROPS_FILE = new File(HOME_DIR + "\\conf\\settings.properties");
		LOGGER.info("SETTINGS_PROPS_FILE:" + SETTINGS_PROPS_FILE);
		if (SETTINGS_PROPS_FILE.exists()) {
			LOGGER.info("加载设置配置文件成功");
		} else {
			LOGGER.error("加载设置配置文件失败");
			return false;
		}
		TEMPLATE_PROPS_FILE = new File(HOME_DIR + "\\conf\\template.properties");
		LOGGER.info("TEMPLATE_PROPS_FILE:" + TEMPLATE_PROPS_FILE);
		if (TEMPLATE_PROPS_FILE.exists()) {
			LOGGER.info("加载上报考勤模板参数文件成功");
		} else {
			LOGGER.error("加载上报考勤模板参数文件失败");
			return false;
		}
		UI_PROPS_FILE = new File(HOME_DIR + "\\conf\\ui.properties");
		LOGGER.info("UI_PROPS_FILE:" + UI_PROPS_FILE);
		if (!UI_PROPS_FILE.exists()) {
			try {
				UI_PROPS_FILE.createNewFile();
				LOGGER.info("加在UI配置文件成功");
			} catch (IOException e) {
				LOGGER.debug("UI配置文件丢失");
				return false;
			}
		}
		return true;
	}

}
