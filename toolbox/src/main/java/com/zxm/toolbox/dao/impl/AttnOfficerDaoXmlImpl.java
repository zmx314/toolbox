package com.zxm.toolbox.dao.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.zxm.toolbox.dao.AttnOfficerDao;
import com.zxm.toolbox.pojo.attn.AttnOfficer;
import com.zxm.toolbox.resources.Resources;
import com.zxm.toolbox.util.XMLUtil;
import org.dom4j.Document;
import org.dom4j.Element;

public class AttnOfficerDaoXmlImpl implements AttnOfficerDao {
	@Override
	public List<AttnOfficer> findAll() {
		List<AttnOfficer> officers = new ArrayList<>();
		AttnOfficer officer;
		Element officerTable = XMLUtil.loadRootElement(Resources.XML_DATA_FILE).element("attnOfficers");
		Iterator<Element> i = officerTable.elementIterator();
		while (i.hasNext()) {
			Element element = i.next();
			officer = new AttnOfficer();
			officer.setCompany(element.attributeValue("company"));
			officer.setGroup(element.attributeValue("group"));
			officer.setName(element.attributeValue("name"));
			officers.add(officer);
		}
		return officers;
	}

	@Override
	public AttnOfficer find(String company, String group) {
		AttnOfficer officer = null;
		Element element = (Element) XMLUtil.loadDocument(Resources.XML_DATA_FILE).selectSingleNode(
				"/data/attnOfficers/attnOfficer[@company='" + company + "'and@group='" + group + "']");
		if (element != null) {
			officer = new AttnOfficer();
			officer.setCompany(element.attributeValue("company"));
			officer.setGroup(element.attributeValue("group"));
			officer.setName(element.attributeValue("name"));
		}
		return officer;
	}

	@Override
	public boolean delete(String company, String group) {
		boolean flag = false;
		Document document = XMLUtil.loadDocument(Resources.XML_DATA_FILE);
		Element officersEl = document.getRootElement().element("attnOfficers");
		Element officerEl = (Element) document.selectSingleNode(
				"/data/attnOfficers/attnOfficer[@company='" + company + "'and@group='" + group + "']");
		if (officerEl != null) {
			officersEl.remove(officerEl);
			XMLUtil.writeDocument(document);
			flag = true;
		}
		return flag;
	}

	@Override
	public boolean save(AttnOfficer officer) {
		boolean flag = false;
		Document document = XMLUtil.loadDocument(Resources.XML_DATA_FILE);
		Element officersEl = document.getRootElement().element("attnOfficers");
		Element officerEl = (Element) document.selectSingleNode("/data/attnOfficers/attnOfficer[@company='"
				+ officer.getCompany() + "'and@group='" + officer.getGroup() + "']");
		if (officerEl == null) {
			officerEl = officersEl.addElement("attnOfficer");
			officerEl.addAttribute("company", officer.getCompany());
			officerEl.addAttribute("group", officer.getGroup());
			officerEl.addAttribute("name", officer.getName());
			XMLUtil.writeDocument(document);
			flag = true;
		}
		return flag;
	}

	@Override
	public boolean update(AttnOfficer officer) {
		boolean flag = false;
		Document document = XMLUtil.loadDocument(Resources.XML_DATA_FILE);
		Element officerEl = (Element) document.selectSingleNode("/data/attnOfficers/attnOfficer[@company='"
				+ officer.getCompany() + "'and@group='" + officer.getGroup() + "']");
		if (officerEl != null) {
			officerEl.addAttribute("company", officer.getCompany());
			officerEl.addAttribute("group", officer.getGroup());
			officerEl.addAttribute("name", officer.getName());
			XMLUtil.writeDocument(document);
			flag = true;
		}
		return flag;
	}
}
