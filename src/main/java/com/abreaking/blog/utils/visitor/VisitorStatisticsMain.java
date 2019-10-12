package com.abreaking.blog.utils.visitor;

import java.io.File;
import java.util.List;

public class VisitorStatisticsMain {

    private static String filePath = "F:\\myWebsite\\logs\\blog-visitor.2019-09.log";

    public static void main(String args[]){
        RecordParser parser = new MyRecordParser();
        LogFileResolver resolver = new LogFileResolver(parser);
        List<Visitor> list = resolver.resolve(new File(filePath));
        System.out.println(list);
    }
}
