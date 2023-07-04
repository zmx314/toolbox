package com.zxm.toolbox.util;

import com.zxm.toolbox.resources.Resources;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;

public class XMLUtil {
	public static Document loadDocument(File xmlFile) {
		Document doc = null;
		try {
			// 读取XML文件,获得document对象
			SAXReader saxReader = new SAXReader();
			doc = saxReader.read(xmlFile);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return doc;
	}

	public static Element loadRootElement(File xmlFile) {
		return loadDocument(xmlFile).getRootElement();
	}

	public static void writeDocument(Document document) {
		OutputFormat format = OutputFormat.createPrettyPrint();
		format.setEncoding("UTF-8");
		try {
			XMLWriter writer = new XMLWriter(new OutputStreamWriter(new FileOutputStream(Resources.XML_DATA_FILE), StandardCharsets.UTF_8),
					format);
			writer.write(document);
			writer.flush();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
