package com.abreaking.blog.utils.visitor;

import com.abreaking.blog.service.VisitorService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.File;
import java.util.List;

@Component
public class VisitorStatisticsMain {

    @Resource
    VisitorService visitorService;

    private static String filePath = "D:\\abreaking\\myWebsite\\logs\\blog-visitor.log";
    private static String filePath1 = "D:\\abreaking\\myWebsite\\logs\\blog-visitor.2019-08.log";
    private static String filePath2 = "D:\\abreaking\\myWebsite\\logs\\blog-visitor.2019-09.log";

    private int count = 0;

    public void main(){
        doResolve(filePath);
        doResolve(filePath1);
        doResolve(filePath2);
    }

    public void doResolve(String file){
        RecordParser parser = new MyRecordParser();
        LogFileResolver resolver = new LogFileResolver(parser);
        resolver.resolve(new File(file),(visitor)->{
            try{
                visitorService.insert(visitor);
                if (count % 128 == 0){
                    System.out.println("已保存"+count+"条数据,当前记录visitor的accessTime为:"+visitor.getAccessTime());
                }
                count++;
            }catch (Exception e){
                e.printStackTrace();
                System.out.println(visitor);
            }
        });
    }


}
