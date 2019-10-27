package com.abreaking.blog.utils.visitor;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import javax.annotation.Resource;
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
