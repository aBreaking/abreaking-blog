package com.abreaking.blog.utils.visitor;

public class Visitor {
    //visitor 的ip地址
    private String ip;
    //根据ip地址，算出是那个城市
    private String city;
    //访问路径
    private String path;
    //代理
    private String agent;
    //访问时间
    private String accessTime;

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

    @Override
    public String toString() {
        return "Visitor{" +
                "ip='" + ip + '\'' +
                ", city='" + city + '\'' +
                ", path='" + path + '\'' +
                ", agent='" + agent + '\'' +
                ", accessTime='" + accessTime + '\'' +
                '}';
    }
}
