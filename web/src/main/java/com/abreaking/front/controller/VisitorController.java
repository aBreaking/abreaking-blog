package com.abreaking.front.controller;

import com.abreaking.common.consts.Consts;
import com.abreaking.core.BaseFrontController;
import com.abreaking.model.Visitor;
import com.abreaking.model.query.VisitorQuery;
import com.abreaking.router.RouterMapping;
import com.abreaking.common.util.AddressUtils;
import com.abreaking.common.util.StringUtils;

import java.util.Date;

/**
 * 针对网站，记录每个访客的信息：ip，城市，代理等信息
 * 后续：该访客访问了那些页面
 * by liwei@abreaking
 */
@RouterMapping(url = "/visitor")
public class VisitorController extends BaseFrontController {

    private static final String COOKIE_NOT_ENABLE = "cookie_not_enable";
    private static final int MAX_AGENT_SECONDS = 43200;  //cookie的存在，最大秒(12小时)


    public void index(){
        renderError(404);
    }

    /**
     * 记录访问者信息，
     * 第一次访问本站时，会为该用户分配一个cookieid，记录该用户在本站的一系列操作
     */
    public void record(){

        //如果是我本人在操作，就没必要记录了
        String user = getCookie("user");
        if(StringUtils.isNotBlank(user)){
            renderAjaxResult("success",Consts.SUCCESS_CODE,"我本人");
            return ;
        }
        String page = getPara("page");
        String cne = getPara("cne");

        Visitor visitor = new Visitor();

        //如果浏览器不支持cookie TODO
        if(StringUtils.isNotBlank(cne)){
            renderAjaxResult("false",Consts.ERROR_CODE_COOKIE_NOT_ENABLE,"浏览器不支持cookie");
            return;
        }

        //一种情况，一些网站的爬虫，百度蜘蛛、谷歌机器人。百度蜘蛛处理
        String crawler = getCrawler(getUserAgent());
        if(crawler!=null){
            String cookieId = crawler;
            Visitor crawl = VisitorQuery.me().findByCookieId(cookieId);
            if(crawl == null){
                crawl.setCookieId(crawler);
                crawl.save();
                return;
            }
            crawl.setVisitTime(new Date());
            crawl.setPages(System.currentTimeMillis()+":"+page+ "," +crawl.getPages());
            crawl.update();
            renderAjaxResult("success",Consts.SUCCESS_CODE,crawler);
            return ;
        }

        //一般的用户，先为其指定一个Cookeid，用于记录该用户的访问的记录
        String cookieId = getCookie("abreaking_visitor");
        if (StringUtils.isBlank(cookieId)) {
            //认为该用户第一次访问了，生成一个uuid来唯一标识该用户
            cookieId = StringUtils.uuid();
            //获取到该用户的ip等信息
            String ipAddress = getIPAddress();
            String city = AddressUtils.getCity(ipAddress);
            String region = AddressUtils.getRegion(ipAddress);

            String userAgent = getUserAgent();
            // 将用户id写入cookie  出现异常好像也没必要取消
            setCookie("abreaking_visitor", cookieId, MAX_AGENT_SECONDS);

            visitor.setCookieId(cookieId);
            visitor.setIp(ipAddress);
            visitor.setCity(city);
            visitor.setRegion(region);
            visitor.setAgent(userAgent);
            visitor.setVisitTime(new Date());
            //某时某刻 访问某个页面,为方便传输并且占用字节小，使用时间戳 timestamp:page
            visitor.setPages(System.currentTimeMillis()+":"+page);
            visitor.save();
            //logo.info("当前访问者的信息");
        } else {
            //如果仍然还是该visitor，也就是该visitor 在MAX_AGENT_SECONDS范围内继续访问
            visitor = VisitorQuery.me().findByCookieId(cookieId);
            //记录访问的页面，以,分割
            visitor.setPages(visitor.getPages() + "," +System.currentTimeMillis()+":"+page);
            visitor.update();
        }
        renderAjaxResult("success",Consts.SUCCESS_CODE);

    }

    //爬虫，目前只是考虑。百度蜘蛛Baiduspider、谷歌机器人Googlebot
    private String getCrawler(String agent){
        agent = agent.toLowerCase();
        for (String crawler : Visitor.WEB_CRAWLER){
            if(agent.indexOf(crawler)!=-1){
                return crawler;
            }
        }
        return null;
    }


}
