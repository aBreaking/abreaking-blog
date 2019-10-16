package com.abreaking.blog.utils.visitor;

import com.abreaking.blog.utils.IPKit;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 日志文件解析
 */
public class LogFileResolver {

    private static final Logger logger = LoggerFactory.getLogger(LogFileResolver.class);

    private RecordParser recordParser;

    public LogFileResolver(RecordParser parser){
        this.recordParser = parser;
    }

    /**
     * 就是读取每行每行的数据，然后再交给{@link RecordParser}去解析
     *
     * 还有一个就是我自己的admin的这种该怎么处理？；
     */
    public List<Visitor> resolve(File logFile) {
        List<Visitor> list = new ArrayList<>();
        resolve(logFile,(visitor)->{
            list.add(visitor);
        });
        return list;
    }

    public void resolve(File logFile,VisitorCallBack visitorCallBack){
        BufferedReader reader  = null;
        try {
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(logFile)));
        } catch (FileNotFoundException e) {
            System.out.println("文件不存在");
            throw new RuntimeException(e);
        }
        /**
         * 一般都是这样，一个visitor来了，访问的不止一个页面，所以需要将这些path放在一个visitor里面就可以了
         */
        Visitor visitor = null;
        while (true){
            try{
                String agentLine = reader.readLine();
                //没有了，就停止循环了。如果还有最后一个visitor，记得保存
                if (StringUtils.isBlank(agentLine)){
                    if (visitor!=null){
                        saveVistor(visitor,visitorCallBack);
                    }
                    break;
                }
                String visitLine = reader.readLine();
                RecordParser.Record record = recordParser.parse(agentLine, visitLine);
                //这里需要对解析后的record进行压缩处理
                if (visitor == null){
                    visitor = adapt(record);
                    continue;
                }

                if (visitorHasRecord(visitor,record)){
                    String path = visitor.getPath() + "->" + record.getPath();
                    String accessTime = visitor.getAccessTime() + "->" + record.getAccessTime();
                    visitor.setPath(path);
                    visitor.setAccessTime(accessTime);
                    continue;
                }
                saveVistor(visitor,visitorCallBack);
                visitor = adapt(record);
            }catch (Exception e){
                System.out.println("resovle出错："+e.getMessage());
                e.printStackTrace();
            }
        }
        try {
            if (reader!=null){
                reader.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveVistor(Visitor visitor,VisitorCallBack visitorCallBack){
        VisitorType.warpVisitor(visitor);
        visitorCallBack.save(visitor);
    }

    /**
     * 判断该visitor是否已经包含了这个record，根据ip、agent来判断
     * @param visitor
     * @param record
     * @return
     */
    private boolean visitorHasRecord(Visitor visitor, RecordParser.Record record){
        return visitor.getIp().equals(record.getIp());
    }


    protected Visitor adapt(RecordParser.Record record){
        Visitor visitor = new Visitor();
        visitor.setIp(record.getIp());
        visitor.setTsid(record.getTsid());
        visitor.setPath(record.getPath());
        visitor.setAccessTime(record.getAccessTime());
        visitor.setAgent(record.getUserAgent());
        String[] cityAndAddr = IPKit.getCityAndAddr(record.getIp());
        visitor.setCity(cityAndAddr[0]);
        visitor.setAddr(cityAndAddr[1]);
        return visitor;
    }

    public interface VisitorCallBack{
        void save(Visitor visitor);
    }
}
