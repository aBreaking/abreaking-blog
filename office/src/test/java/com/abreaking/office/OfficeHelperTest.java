package com.abreaking.office;

import org.junit.Test;

import java.io.File;
import java.util.List;

/**
 * @{USER}
 * @{DATE}
 */
public class OfficeHelperTest {
    static OfficeHelper officeHelper = new OfficeHelper();

    @Test
    public void buildTest() throws Exception {
        String templateName = "KL-19-C106保利狮子湖东区外线工程-重庆瑞普.xml";
        File file = new File("C:\\Users\\MI\\Desktop\\jiamei\\test\\original.xlsx");
        OfficeHelper helper = new OfficeHelper();
        String retFile = helper.build(templateName, file);
        System.out.println(retFile);
    }

    @Test
    public void addWordTemplateTest() throws Exception {
        String originFile = "C:\\Users\\MI\\Desktop\\jiamei\\test\\KL-19-C106保利狮子湖东区外线工程-重庆瑞普.xml";
        File file = new File(originFile);
        officeHelper.addWordTemplate(file.getName(),file);
    }

    @Test
    public void getAllTemplateTest(){
        List<String> list = officeHelper.getAllTemplate();
        System.out.println(list);
    }
}
