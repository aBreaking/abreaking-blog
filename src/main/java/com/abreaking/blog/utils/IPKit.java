package com.abreaking.blog.utils;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

/**
 * ip工具类
 * Created by BlueT on 2017/3/9.
 */
public class IPKit {
    /**
     * @param request 请求
     * @return IP Address
     */
    public static String getIpAddrByRequest(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    /**
     * @return 本机IPSocketException
     * @throws SocketException
     */
    public static String getRealIp() throws SocketException {
        String localip = null;// 本地IP，如果没有配置外网IP则返回它
        String netip = null;// 外网IP

        Enumeration<NetworkInterface> netInterfaces = NetworkInterface.getNetworkInterfaces();
        InetAddress ip = null;
        boolean finded = false;// 是否找到外网IP
        while (netInterfaces.hasMoreElements() && !finded) {
            NetworkInterface ni = netInterfaces.nextElement();
            Enumeration<InetAddress> address = ni.getInetAddresses();
            while (address.hasMoreElements()) {
                ip = address.nextElement();
                if (!ip.isSiteLocalAddress() && !ip.isLoopbackAddress() && !ip.getHostAddress().contains(":")) {// 外网IP
                    netip = ip.getHostAddress();
                    finded = true;
                    break;
                } else if (ip.isSiteLocalAddress() && !ip.isLoopbackAddress() && !ip.getHostAddress().contains(":")) {// 内网IP
                    localip = ip.getHostAddress();
                }
            }
        }

        if (netip != null && !"".equals(netip)) {
            return netip;
        } else {
            return localip;
        }
    }

    /**
     * 根据ip地址获取是哪个城市及具体地址
     * @return
     */
    public static String[] getCityAndAddr(String ip){
        String requestUrl = "https://whois.pconline.com.cn/ipJson.jsp";
        String param = "json=true&ip="+ip;
        String result = null;
        try {
            result = HttpUtils.sendGet(requestUrl, param,"gbk");
        } catch (IOException e) {
            try {
                //重试一次
                System.out.println("获取城市信息失败，2秒后重试一次");
                Thread.sleep(2000);
                result = HttpUtils.sendGet(requestUrl, param,"gbk");
            } catch (Exception e1) {
                System.out.println("重试还是失败！");
                throw new RuntimeException(e);
            }
        }
        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(result, JsonObject.class);
        String[] cityAndAddr = new String[2];
        cityAndAddr[0] = jsonObject.get("city").getAsString();
        cityAndAddr[1] = jsonObject.get("addr").getAsString();

        return cityAndAddr;
    }

}
