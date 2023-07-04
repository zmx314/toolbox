package com.zxm.toolbox;

import com.zxm.toolbox.resources.Resources;
import com.zxm.toolbox.util.XMLUtil;
import org.dom4j.Element;

import java.util.Iterator;

public class Test {
    public static void main(String[] args) {
        Resources.init();
        Element el = XMLUtil.loadRootElement(Resources.XML_DATA_FILE).element("attnOfficers");
        Iterator<Element> i = el.elementIterator();
        while (i.hasNext()){
            System.out.println(i.next());
        }
    }
}
