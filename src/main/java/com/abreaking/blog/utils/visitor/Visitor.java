package com.abreaking.blog.utils.visitor;

/**
 * 访客的基本信息
 * @author liwei_paas
 * @date 2019/10/14
 */
public class Visitor {

    //用时间戳作为主键
    private Long tsid;
    //visitor 的ip地址
    private String ip;
    //根据ip地址，算出是那个城市
    private String city;
    //详细位置信息
    private String addr;
    //访问路径
    private String path;
    //代理
    private String agent;
    //访问时间
    private String accessTime;

    public Long getTsid() {
        return tsid;
    }

    public void setTsid(Long tsid) {
        this.tsid = tsid;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getAgent() {
        return agent;
    }

    public void setAgent(String agent) {
        this.agent = agent;
    }

    public String getAccessTime() {
        return accessTime;
    }

    public void setAccessTime(String accessTime) {
        this.accessTime = accessTime;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    @Override
    public String toString() {
        return "Visitor{" +
                "tsid=" + tsid +
                ", ip='" + ip + '\'' +
                ", city='" + city + '\'' +
                ", addr='" + addr + '\'' +
                ", path='" + path + '\'' +
                ", agent='" + agent + '\'' +
                ", accessTime='" + accessTime + '\'' +
                '}';
    }
}
