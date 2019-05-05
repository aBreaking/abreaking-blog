package com.abreaking.office;

import com.abreaking.office.word.XmlParser;
import org.dom4j.Document;
import org.dom4j.Element;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @{USER}
 * @{DATE}
 */
public class XmlParserTest {

    static String xmlFile = "C:\\Users\\MI\\Desktop\\table.xml";
    static String outFile = "C:\\Users\\MI\\Desktop\\table_new.xml";
    static XmlParser xmlParser = new XmlParser();
    static OfficeHelper officeHelper = new OfficeHelper();

    @Test
    public void test01() throws Exception {
        Document document = xmlParser.parse(new FileInputStream(xmlFile));
        Element part = (Element) document.getRootElement().elements("part").get(1);
        Element tblElement = part.element("xmlData").element("document").element("body").element("tbl");
        List<Map<String, String>> list = getList();
        xmlParser.parseWordTable(tblElement,list);
        officeHelper.save(document,new File(outFile));
    }


    private List<Map<String,String>> getList(){
        List<Map<String,String>> list= new ArrayList<>();
        Map<String,String> map1 = new HashMap<>();
        map1.put("姓名","张三");
        map1.put("年龄","12");
        map1.put("电话号码","10086");
        list.add(map1);
        Map<String,String> map2 = new HashMap<>();
        map2.put("姓名","李四");
        map2.put("年龄","19");
        map2.put("电话号码","112345");
        list.add(map2);
        return list;
    }
}
