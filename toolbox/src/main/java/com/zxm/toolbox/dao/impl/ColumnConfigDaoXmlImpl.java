package com.zxm.toolbox.dao.impl;

import com.zxm.toolbox.resources.Resources;
import com.zxm.toolbox.util.XMLUtil;
import com.zxm.toolbox.pojo.gt.ColumnProperties;
import org.dom4j.Element;

import java.util.ArrayList;
import java.util.List;

public class ColumnConfigDaoXmlImpl {

	public static List<ColumnProperties> findAll() {
		List<ColumnProperties> list = new ArrayList<>();
		Element configsEl = XMLUtil.loadRootElement(Resources.XML_DATA_FILE).element("columnConfigs");
		List<Element> elements = configsEl.elements();
		ColumnProperties colProp;
		for (Element element : elements) {
			colProp = new ColumnProperties();
			colProp.setName(element.attributeValue("name"));
			colProp.setResource(element.attributeValue("resource"));
			colProp.setMethod(element.attributeValue("method"));
			colProp.setReturnType(element.attributeValue("returnType"));
			colProp.setPaxType(element.attributeValue("paxType"));
			colProp.setTicketDetailAttr(element.attributeValue("ticketDetailAttr"));
			colProp.setNeedSum(element.attributeValue("needSum").equals("True"));
			colProp.setStyle(element.attributeValue("style"));
			colProp.setColumnSize(Integer.parseInt(element.attributeValue("columnSize")));
			list.add(colProp);
		}
		return list;
	}
}
