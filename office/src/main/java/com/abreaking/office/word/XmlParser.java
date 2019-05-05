package com.abreaking.office.word;

import com.abreaking.office.OfficeHelper;
import org.dom4j.*;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @{USER}
 * @{DATE}
 */
public class XmlParser {

    /**
     * 解析word 里面的table.主要节点关系如下：
     *
     * @param tblElement w:tbl节点
     */
    public void parseWordTable(Element tblElement, List<Map<String,String>> mapList){
        //w:tr
        List<Element> tr = tblElement.elements("tr");
        if (tr.size()==0){
            return;
        }
        //如果表格只有一行，就以第一行作为作为模板,否则就以第二行作为模板
        Element tempElement = tr.get(1).createCopy();
        tblElement.clearContent();
        //而后标题添加到tblElement中
        tblElement.add(tr.get(0));
        //开始组装表格
        for (int i=0;i<mapList.size();i++){
            Element newElement = tempElement.createCopy();
            Attribute paraId = newElement.attribute("paraId");
            if (paraId != null){
                paraId.setValue(randomParaId());
            }

            //替换$里面的内容
            replace$AtTr(newElement,mapList.get(i));
            //添加到tbl节点中去
            tblElement.add(newElement);
        }
        //其他部分继续组装
        for (int i=2;i<tr.size();i++){
            tblElement.add(tr.get(i));
        }
    }

    private void replace$AtTr(Element trElement,Map<String,String> map){
        List<Element> tc = trElement.elements("tc");
        for (Element tcElement: tc){
            Element tElement = tcElement.element("p").element("r").element("t");
            String text = tElement.getText();
            if (text.indexOf("$")!=-1){
                String key = text.substring(text.indexOf("${")+2,text.indexOf("}"));
                if (map.containsKey(key)){
                    String newText = map.get(key);
                    tElement.setText(newText);
                }else{
                    System.out.println("没有找到 "+key+" 这个key");
                }
            }
        }
    }

    public Document parse(InputStream xmlFileIn) throws Exception {
        SAXReader saxReader = new SAXReader();
        Document document = saxReader.read(xmlFileIn);
        Element rootElement = document.getRootElement();
        List<Element> elements = rootElement.elements();
        Element element = elements.get(1);
        Element xmlData = element.element("xmlData");
        Element wDocument = xmlData.element("document");
        Element body = wDocument.element("body");
        List<Element> pElement = body.elements("p");
        //正常行的处理
        for (Element e : pElement){
            parseP(e);
        }
        //表格行的处理
        Element tblElement = body.element("tbl");
        if (tblElement != null){
            List<Element> trElement = tblElement.elements("tr");
            for (Element tr :trElement){
                List<Element> tcElement = tr.elements("tc");
                for (Element tc : tcElement){
                    List<Element>  pElements = tc.elements("p");
                    for (Element p : pElements){
                        parseP(p);
                    }
                }
            }
        }

        return document;
    }

    /**
     * 将w:p 下面的w:t 中断开的${XX} 拼接在一起
     * @param p
     */
    private void parseP(Element p){
        List<Element> rList = p.elements("r");
        //找到第一含有$的t
        Element $Element = null;
        int rIndex = 0;

        for (int i=0;i<rList.size();i++){
            Element tElement = rList.get(i).element("t");

            //找到了$开头的
            if (tElement.getText().indexOf("$")!=-1){
                //记录当前这个element,还有位置
                $Element = tElement;
                rIndex = i;
                continue;
            }

            //此刻说明前面已经找到了$
            if ($Element != null){
                $Element.addText(tElement.getText());
                tElement.setText("");
                //现在是}，说明结束了，装配完毕了
                if (tElement.getText().indexOf("}")!=-1){
                    Element t = rList.get(rIndex).element("t");
                    t.setText($Element.getText());
                    $Element=null;
                    rIndex = 0;
                }

            }

        }
    }

    /**
     * 随机一个word的paraId
     * @return
     */
    public static String randomParaId(){
        UUID uuid = UUID.randomUUID();
        String s = uuid.toString().toUpperCase();
        return s.substring(0,s.indexOf("-"));
    }


    public static void _main(String args[]) throws Exception {
        String xmlFile = "C:\\Users\\MI\\Desktop\\table.xml";
        String outFile = "C:\\Users\\MI\\Desktop\\table_new.xml";
        SAXReader reader = new SAXReader();
        XmlParser parser = new XmlParser();
        Document document = reader.read(new File(xmlFile));
        Element rootElement = document.getRootElement();
        Element part = (Element) rootElement.elements("part").get(1);
        Element element = part.element("xmlData").element("document").element("body").element("tbl");
        /*parser.parseWordTable(element);*/

        OfficeHelper helper = new OfficeHelper();
        helper.save(document,new File(outFile));

    }

}
