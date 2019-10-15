package com.abreaking.blog.utils.visitor;

import java.io.File;
import java.util.List;

public class VisitorStatisticsMain {

    private static String filePath = "D:\\abreaking\\myWebsite\\logs\\blog-visitor.log";

    public static void main(String args[]){
        RecordParser parser = new MyRecordParser();
        LogFileResolver resolver = new LogFileResolver(parser);
        List<Visitor> list = resolver.resolve(new File(filePath));
        System.out.println(list);
    }
}
