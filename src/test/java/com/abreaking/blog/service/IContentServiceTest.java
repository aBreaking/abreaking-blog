package com.abreaking.blog.service;

import com.github.pagehelper.PageInfo;
import com.abreaking.blog.model.Vo.ContentVo;
import com.abreaking.blog.model.Vo.ContentVoExample;
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
public class IContentServiceTest {
    @Resource
    private IContentService contentService;

    @Test
    public void test(){
        ContentVoExample contentVoExample = new ContentVoExample();
        contentVoExample.setOrderByClause("created desc");
        contentVoExample.createCriteria().andTypeEqualTo("poetry");
        PageInfo<ContentVo> info = contentService.getArticlesWithpage(contentVoExample, 1, 9);
        System.out.println(info.getList());

    }
}
