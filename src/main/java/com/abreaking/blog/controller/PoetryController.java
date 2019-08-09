package com.abreaking.blog.controller;

import com.github.pagehelper.PageInfo;
import com.abreaking.blog.dto.Types;
import com.abreaking.blog.model.Vo.ContentVo;
import com.abreaking.blog.model.Vo.ContentVoExample;
import com.abreaking.blog.service.IContentService;
import com.abreaking.blog.utils.Commons;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;

/**
 *
 * @author liwei_paas
 * @date 2019/6/21
 */
@Controller
@RequestMapping("/poetry")
public class PoetryController extends BaseController{
    private static final Logger LOGGER = LoggerFactory.getLogger(PoetryController.class);

    private static final int LIMIT = 9;

    @Resource
    private IContentService contentService;

    @GetMapping
    public String index(Model model){
        model.addAttribute("poetryList", query(1).getList());
        model.addAttribute("pageNum",1);
        return this.render("poetry");
    }

    @RequestMapping("/page/{pageNum}")
    @ResponseBody
    public PageInfo pageNext(@PathVariable("pageNum") int pageNum){
        return query(pageNum);
    }


    private PageInfo<ContentVo> query(int pageNum) {
        ContentVoExample contentVoExample = new ContentVoExample();
        contentVoExample.setOrderByClause("created desc");
        contentVoExample.createCriteria().andTypeEqualTo("poetry");
        contentVoExample.createCriteria().andTypeEqualTo(Types.ARTICLE.getType());
        PageInfo<ContentVo> info = contentService.getArticlesWithpage(contentVoExample, pageNum, LIMIT);
        convertMarkDown2Html(info);
        return info;
    }

    /**
     * markdown的文本转换
     */
    private void convertMarkDown2Html(PageInfo<ContentVo> pageInfos){
        List<ContentVo> list = pageInfos.getList();
        for (ContentVo contentVo : list){
            String article = Commons.article(contentVo.getContent());
            contentVo.setContent(article);
        }
        pageInfos.setList(list);
    }
}
