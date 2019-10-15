package com.abreaking.blog.utils.visitor;

import com.abreaking.blog.service.VisitorService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.io.File;
import java.util.List;

/**
 * @{USER}
 * @{DATE}
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class VisitorTest {

    @Resource
    VisitorService visitorService;

    private static String filePath = "D:\\abreaking\\myWebsite\\logs\\blog-visitor.log";

    @Test
    public void test01(){
        RecordParser parser = new MyRecordParser();
        LogFileResolver resolver = new LogFileResolver(parser);
        resolver.resolve(new File(filePath),(visitor)->{
            try{

                visitorService.insert(visitor);
            }catch (Exception e){
                e.printStackTrace();
                System.out.println(visitor);
            }
        });
    }
}
