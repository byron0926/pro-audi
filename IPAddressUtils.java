package com.dora.audi.common.util;

import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;

/**
 * @desc: ip分析工具
 * @author： kidy
 * @createtime： 4/20/20189:36 AM
 * @modify by： ${user}
 * @modify time： 4/20/20189:36 AM
 * @desc of modify：
 * @throws:
 */
public class IPAddressUtils {
    private static Logger logger = LoggerFactory.getLogger(IPAddressUtils.class);
    private static final String URL = "http://ip.taobao.com/service/getIpInfo.php";

    /**
     * 解析IP信息
     * @param ip
     * @return
     */
    public final static String getAddress(String ip){
        StringBuffer sb = new StringBuffer(ip);
        try {
            String rr = HttpClient.getResult(URL,"format=json&ip="+ip,"utf-8");
            JSONObject obj = JSONObject.parseObject(rr);
            logger.info("物理地址请求返回数据:"+rr);
            if (obj.containsKey("code")&&obj.getInteger("code")==0&&obj.containsKey("data")){
                JSONObject res = obj.getJSONObject("data");
                StringBuffer s = new StringBuffer();
                s.append("(");
                s.append(res.get("country")).append(res.get("region")).append(res.get("area"));
                s.append(res.get("city")).append(res.get("isp")).append(")");
                if (s.toString().contains("内网IP")){
                    sb.append("(内网IP)");
                }else {
                    sb.append(s.toString());
                }
            }
        } catch (Exception e) {
            logger.error("获取ip物理地址异常:",e);
        }
        logger.info("登录地址:"+sb.toString());
        return sb.toString();
    }

    /**
     * 获取请求主机IP地址,如果通过代理进来，则透过防火墙获取真实IP地址;
     *
     * @param request
     * @return
     */
    public final static String getIpAddress(HttpServletRequest request){
        // 获取请求主机IP地址,如果通过代理进来，则透过防火墙获取真实IP地址
        String ip = request.getHeader("X-Forwarded-For");
        if (logger.isInfoEnabled()) {
            logger.info("getIpAddress(HttpServletRequest) - X-Forwarded-For - String ip=" + ip);
        }

        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("Proxy-Client-IP");
                if (logger.isInfoEnabled()) {
                    logger.info("getIpAddress(HttpServletRequest) - Proxy-Client-IP - String ip=" + ip);
                }
            }
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("WL-Proxy-Client-IP");
                if (logger.isInfoEnabled()) {
                    logger.info("getIpAddress(HttpServletRequest) - WL-Proxy-Client-IP - String ip=" + ip);
                }
            }
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("HTTP_CLIENT_IP");
                if (logger.isInfoEnabled()) {
                    logger.info("getIpAddress(HttpServletRequest) - HTTP_CLIENT_IP - String ip=" + ip);
                }
            }
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("HTTP_X_FORWARDED_FOR");
                if (logger.isInfoEnabled()) {
                    logger.info("getIpAddress(HttpServletRequest) - HTTP_X_FORWARDED_FOR - String ip=" + ip);
                }
            }
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getRemoteAddr();
                if (logger.isInfoEnabled()) {
                    logger.info("getIpAddress(HttpServletRequest) - getRemoteAddr - String ip=" + ip);
                }
            }
        } else if (ip.length() > 15) {
            String[] ips = ip.split(",");
            for (int index = 0; index < ips.length; index++) {
                String strIp = ips[index];
                if (!("unknown".equalsIgnoreCase(strIp))) {
                    ip = strIp;
                    break;
                }
            }
        }
        return ip;
    }
}
