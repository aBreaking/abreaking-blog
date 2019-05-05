package com.abreaking.office;

import com.abreaking.office.resource.Resource;
import com.abreaking.office.word.export.WordStreamExporter;
import org.junit.Test;

import java.io.*;

import static com.abreaking.office.Runner.build;
import static com.abreaking.office.Runner.getFileResource;
import static com.abreaking.office.Runner.templateDir;

/**
 * @{USER}
 * @{DATE}
 */
public class RunnerTest {

    public void test01(){
    }

    /**
     * 测试模板
     */
    @Test
    public void testTemplate() throws IOException {
        String templateName = templateDir+"KL-19-C106保利狮子湖东区外线工程-重庆瑞普.xml";
        WordStreamExporter exporter = new WordStreamExporter(new FileInputStream(templateName));
        System.out.println(exporter);
    }

    /**
     * 生成合同
     * @throws Exception
     */
    @Test
    public void testBuild() throws Exception {
        final String excelFile = "C:\\Users\\MI\\Desktop\\jiamei\\test\\original.xlsx";
        Resource resource = getFileResource(excelFile);

        final String templateName = "KL-19-C106保利狮子湖东区外线工程-重庆瑞普.xml";
        String wordName = resource.name().substring(0,resource.name().indexOf("."))+".docx";
        final String targetWord = "C:\\Users\\MI\\Desktop\\"+wordName;
        Writer writer = new FileWriter(new File(targetWord));
        build(resource,templateName,writer);
        writer.close();
    }
}
