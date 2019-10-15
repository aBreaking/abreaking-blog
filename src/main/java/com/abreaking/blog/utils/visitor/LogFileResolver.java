package com.abreaking.blog.utils.visitor;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;
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
        try {

            BufferedReader reader  = new BufferedReader(new InputStreamReader(new FileInputStream(logFile)));
            while (true){
                String agentLine = reader.readLine();
                //没有了，就停止循环了
                if (StringUtils.isBlank(agentLine)){
                    break;
                }
                String visitLine = reader.readLine();
                Visitor visitor = recordParser.parse(agentLine, visitLine);
                visitorCallBack.save(visitor);
            }
        } catch (FileNotFoundException e) {
            logger.error("找不到该文件"+logFile.getAbsolutePath(),e);
            throw new RuntimeException(e);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public interface VisitorCallBack{
        void save(Visitor visitor);
    }
}
