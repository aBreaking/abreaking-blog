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
    Record parse(String agentRecord,String visitRecord);

    /**
     * 解析后的结果。不对解析的字段做处理
     * @author liwei_paas
     * @date 2019/10/16
     */
    class Record{
        Long tsid;
        String accessTime;
        String userAgent;
        String ip;
        String path;

        public Long getTsid() {
            return tsid;
        }

        public void setTsid(Long tsid) {
            this.tsid = tsid;
        }

        public String getAccessTime() {
            return accessTime;
        }

        public void setAccessTime(String accessTime) {
            this.accessTime = accessTime;
        }

        public String getUserAgent() {
            return userAgent;
        }

        public void setUserAgent(String userAgent) {
            this.userAgent = userAgent;
        }

        public String getIp() {
            return ip;
        }

        public void setIp(String ip) {
            this.ip = ip;
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }
    }
}
