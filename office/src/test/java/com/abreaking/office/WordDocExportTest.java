package com.abreaking.office;

import freemarker.cache.URLTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.junit.Test;
import sun.misc.BASE64Encoder;

import java.io.*;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @{USER}
 * @{DATE}
 */
public class WordDocExportTest {

    public static Template getTemplate(String file) throws IOException {
        Configuration configuration = new Configuration();

        /** 设置编码 **/
        configuration.setDefaultEncoding("utf-8");
        configuration.setTemplateLoader(new URLTemplateLoader() {
            @Override
            protected URL getURL(String name) {
                return WordDocExportTest.class.getClassLoader().getResource("template/"+name);
            }
        });
        /** 加载模板 **/
        return configuration.getTemplate(file);
    }

    @Test
    public void getAllIdentifier() throws IOException {
        String s = getTemplate("").toString();
        Pattern pattern = Pattern.compile("\\$\\{[\\w]*\\}");
        Matcher matcher = pattern.matcher(s);
        while(matcher.find()){
            System.out.println(matcher.group());
        }

    }

    public static void main(String[] args) throws Exception{
        String templateName = "";

        Template template = getTemplate(templateName);

        /** 准备数据 **/
        Map<String,String> dataMap = new HashMap<>();

        //插入图片,文件流
        FileInputStream inputStream = new FileInputStream("D:\\abreaking\\myWebsite\\图片素材\\卷轴04.jpg");
        byte[] bytes = new byte[inputStream.available()];
        inputStream.read(bytes);
        inputStream.close();
        /** 进行base64位编码 **/
        BASE64Encoder encoder = new BASE64Encoder();
        /** 在ftl文件中有${textDeal}这个标签**/
        dataMap.put("name","张三123");
        dataMap.put("title","标题");
        /** 图片数据**/
        dataMap.put("image",encoder.encode(bytes));

        /** 指定输出word文件的路径 **/
        String outFilePath = "C:\\Users\\MI\\Desktop\\myFreeMarker.doc";
        File docFile = new File(outFilePath);
        FileOutputStream fos = new FileOutputStream(docFile);
        Writer out = new BufferedWriter(new OutputStreamWriter(fos, "utf-8"),10240);
        template.process(dataMap,out);

        if(out != null){
            out.close();
        }
    }
}
