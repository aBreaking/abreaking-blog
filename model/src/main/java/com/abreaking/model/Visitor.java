package com.abreaking.model;


import com.abreaking.model.base.BaseVisitor;
import com.abreaking.model.core.Table;
import com.abreaking.common.util.DateUtils;

import java.util.ArrayList;
import java.util.List;

@Table(tableName = "visitor", primaryKey = "id")
public class Visitor  extends BaseVisitor<Visitor> {

    public static final String TYPE_COMMENT = "visitor";
    public static final String[] WEB_CRAWLER = {"baiduspider","googlebot"};

    public String getDay(){
        String pages = getPages();
        String s = pages.substring(0, 13);
        return DateUtils.formatTimestamp(s,"yyyy-MM-dd");
    }
    public String getLandPage(){
        String pages = getPages();
        int i = pages.indexOf(":");
        int j = pages.indexOf(",");
        if(j==-1){
            return pages.substring(i+1,pages.length());
        }
        return pages.substring(i+1,j);
    }

    public List<String> getVisitDetail(){
        String pages = getPages();
        String[] _p = pages.split(",");
        ArrayList<String> list = new ArrayList();
        //如果是百度蜘蛛
        if("baiduspider".equals(getCookieId())){
            list.add("总共访问"+_p.length+"次");
            return list;
        }
        for(String s:_p){
            String[] _s = s.split(":");
            String timestamp = _s[0];
            String page = _s.length>1?_s[1]:"未知页面";

            String detail = DateUtils.formatTimestamp(timestamp,"HH:mm:ss") +"访问了"+page;
            list.add(detail);
        }
        return list;
    }

    public static void main(String args[]){

        String pages = "1530606669722:博客首页1";
        int i = pages.indexOf(":");
        int j = pages.indexOf(",");
        System.out.println(i+","+j);
        if(j==-1){
            System.out.println(pages.substring(i+1,pages.length()));
        }else{
            System.out.println(pages.substring(i+1,j));
        }

    }



}
