package com.abreaking.blog.utils.visitor;

import org.apache.commons.lang3.StringUtils;

/**
 * 几种访客的类型
 * @author liwei_paas
 * @date 2019/10/16
 */
public class VisitorType {

    /**
     * 正常的访客
     */
    public static final int NORMAL = 0;


    /**
     * 某些爬虫，比如百度、谷歌
     */
    public static final int SPIDER = 1;

    /**
     * 境外，可能国外的不明攻击
     */
    public static final int ABROAD = 2;

    /**
     * 我自己
     */
    public static final int ME = 3;

    /**
     * 登录admin的visitor
     */
    public static final int ADMIN = 4;


    private static final String BOT_REGEX = ".*compatible.*http.*//.*bot.*";

    public static void warpVisitor(Visitor visitor){
        String agent = visitor.getAgent();
        if (agent.indexOf("spider")!=-1 || agent.matches(BOT_REGEX)){
            visitor.setType(SPIDER);
            return;
        }
        if (StringUtils.isBlank(visitor.getCity())){
            visitor.setType(ABROAD);
            return;
        }
        if (isMe(visitor.getAgent(),visitor.getCity())){
            visitor.setType(ME);
            return;
        }
        if (visitor.getPath().indexOf("admin")!=-1){
            visitor.setType(ADMIN);
            return;
        }
        visitor.setType(NORMAL);
    }

    public static boolean isMe(String agent,String city){
        if (agent.matches(".*Redmi K20.*XiaoMi.*")){
            return true;
        }
        if (city.equals("成都市")&&agent.matches("Mozilla/5.0 .* AppleWebKit/537.36 .* Chrome/77.0.* Safari/537.36")){
            return true;
        }
        return false;
    }

}
