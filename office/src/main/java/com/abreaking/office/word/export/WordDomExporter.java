package com.abreaking.office.word.export;

import com.abreaking.office.word.XmlParser;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.XMLWriter;

import java.io.IOException;
import java.io.Writer;
import java.util.List;
import java.util.Map;

/**
 * 生成合同前的dom4j处理，如对表格数据进行下处理
 * @{USER}
 * @{DATE}
 */
public class WordDomExporter {

    private Document document;
    XMLWriter xmlWriter;

    public WordDomExporter(Document document){
        this.document = document;
    }

    public void export(List<Map<String,String>> mapList, Writer writer) throws Exception {
        Element rootElement = document.getRootElement();
        Element part = (Element) rootElement.elements("part").get(1);
        Element tblElement = part.element("xmlData").element("document").element("body").element("tbl");
        XmlParser xmlParser = new XmlParser();
        xmlParser.parseWordTable(tblElement,mapList);

        xmlWriter = new XMLWriter(writer);
        xmlWriter.write(document);
    }

    public void close() throws IOException {
        xmlWriter.close();
    }
}
