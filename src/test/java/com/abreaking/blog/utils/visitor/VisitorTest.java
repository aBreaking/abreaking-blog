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
    VisitorStatisticsMain visitorStatisticsMain;


    @Test
    public void test01(){
        visitorStatisticsMain.main();
    }
}
