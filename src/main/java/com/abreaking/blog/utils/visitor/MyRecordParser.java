package com.abreaking.blog.utils.visitor;

import com.abreaking.blog.utils.IPKit;

public class MyRecordParser implements RecordParser {

    protected Visitor visitor ;

    private static final String DATE_PATTERN = "yyyy-MM-dd HH:mm:ss";
    private static final String TAG_OF_USERAGENT = "UserAgent:";
    private static final String TAG_OF_PATH = "用户访问地址:";
    private static final String TAG_OF_IP = "来路地址:";

    public MyRecordParser(){
        this.visitor = new Visitor();
    }

    @Override
    public Visitor parse(String agentRecord, String visitRecord) {
        parseAgentRecord(agentRecord);
        parseVisitRecord(visitRecord);
        return visitor;
    }

    protected void parseAgentRecord(String agentRecord){
        int indexOfAgent = agentRecord.indexOf(TAG_OF_USERAGENT);
        if (indexOfAgent==-1){
            throw new RuntimeException("该条记录没有包含UserAgent");
        }
        String accessTime = agentRecord.substring(0, DATE_PATTERN.length());
        visitor.setAccessTime(accessTime);
        String userAgent = agentRecord.substring(indexOfAgent + TAG_OF_USERAGENT.length() + 1);
        visitor.setAgent(userAgent);
    }

    protected void parseVisitRecord(String record){
        int indexOfPath = record.indexOf(TAG_OF_PATH);
        if (indexOfPath==-1){
            throw new RuntimeException("该条记录没有包含用户访问地址");
        }
        String recordAfterPath = record.substring(indexOfPath+ TAG_OF_PATH.length() + 1);
        String path = recordAfterPath.substring(0, recordAfterPath.indexOf(","));
        visitor.setPath(path);
        int indexOfIp = recordAfterPath.indexOf(TAG_OF_IP);
        String ip = recordAfterPath.substring(indexOfIp + TAG_OF_IP.length() + 1);
        visitor.setIp(ip);
        String city = IPKit.getCityByIp(ip);
        visitor.setCity(city);
    }


}
