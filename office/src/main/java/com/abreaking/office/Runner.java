package com.abreaking.office;

import com.abreaking.office.excel.ExcelReader;
import com.abreaking.office.resource.Resource;
import com.abreaking.office.word.export.WordStreamExporter;
import com.abreaking.office.word.XmlParser;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.dom4j.Document;
import java.io.*;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Map;


/**
 * @{USER}
 * @{DATE}
 */
public class Runner {
    /**
     * 生成模板的路径
     */
    public static String templateDir= "template/";
    static {
        URL url = Runner.class.getClassLoader().getResource("config.properties");
        String path = url.getPath();
        path = path.substring(0,path.lastIndexOf("/")+1);
        try {
            templateDir = URLDecoder.decode(path + templateDir,"utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据上传的excel文件，生成合同
     * @param excelResource 上传的excel 文件
     * @param templateName 指定模板
     */
    public static void build(Resource excelResource, String templateName, Writer writer) throws Exception {
        //将源excel生成数据
        Workbook workbook = new XSSFWorkbook(excelResource.getInputStream());
        ExcelReader reader = new ExcelReader();
        Map<String, String> dataMap = reader.parse(workbook).getBaseDataMap();

        //指定的模板
        WordStreamExporter templateExporter = new WordStreamExporter(new FileInputStream(templateDir+templateName));
        templateExporter.export(dataMap,writer);
    }

    /**
     * 根据源word的xml文件，生成模板文件，保存在classpath:template/  目录下
     */
    public static void addWordTemplate(Resource origalWordXmlResource) throws Exception {
        //解析源文件
        XmlParser parser = new XmlParser();
        Document document = parser.parse(origalWordXmlResource.getInputStream());
        //保存解析后的文档
        File file = new File(templateDir+origalWordXmlResource.name());
        //如果这个文件存在，就删除掉
        if (!file.exists()){
            file.createNewFile();
        }
        //文件保存在这个目录
        /*parser.save(document,file);*/
    }



    public static void main(String args[]) throws Exception {
        //生成模板文件在template目录下去
        final String originDocXmlFile = "C:\\Users\\MI\\Desktop\\jiamei\\test\\KL-19-C106保利狮子湖东区外线工程-重庆瑞普.xml";
        Resource resource = getFileResource(originDocXmlFile);
        addWordTemplate(resource);
    }

    public static Resource getFileResource(final String file){
        return new Resource() {
            @Override
            public InputStream getInputStream() {
                try {
                    return new FileInputStream(file);
                } catch (FileNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public String name() {
                return new File(file).getName();
            }
        };
    }


}
