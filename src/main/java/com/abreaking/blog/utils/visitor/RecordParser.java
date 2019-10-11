package com.abreaking.blog.utils.visitor;

/**
 * 每一条访问记录的解析方式
 */
public interface RecordParser {

    /**
     * 解析的方法。对每个访问者的访问记录，日志文件里面主要是记录两个部分：用户代理；访问路径及ip
     * @param agentRecord 用户代理的这条记录怎么解析
     * @param visitRecord 访问路径及ip这条记录怎么解析
     * @return
     */
    Visitor parse(String agentRecord,String visitRecord);
}
