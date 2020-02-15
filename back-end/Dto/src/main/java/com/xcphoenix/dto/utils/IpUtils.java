package com.xcphoenix.dto.utils;

import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * @author      xuanc
 * @date        2020/1/31 上午10:29
 * @version     1.0
 */ 
public class IpUtils {

    private IpUtils() {}

    /**
     * 获取客户端真实 IP
     * From: http://madaimeng.com/article/Spring-Web/
     */
    public static String getIp() {
        // 从RequestContextHolder中获取HttpServletRequest
        HttpServletRequest httpServletRequest = ContextHolderUtils.getRequest();
        String xIp = httpServletRequest.getHeader("X-Real-IP");
        String xFor = httpServletRequest.getHeader("X-Forwarded-For");

        String unknownFlag = "unknown";

        if(StringUtils.isNotEmpty(xFor) && !unknownFlag.equalsIgnoreCase(xFor)){
            //多次反向代理后会有多个ip值，第一个ip才是真实ip
            int index = xFor.indexOf(",");
            if(index != -1){
                return xFor.substring(0,index);
            }else{
                return xFor;
            }
        }
        xFor = xIp;
        if(StringUtils.isNotEmpty(xFor) && !unknownFlag.equalsIgnoreCase(xFor)){
            return xFor;
        }
        if (StringUtils.isBlank(xFor) || unknownFlag.equalsIgnoreCase(xFor)) {
            xFor = httpServletRequest.getHeader("Proxy-Client-IP");
        }
        if (StringUtils.isBlank(xFor) || unknownFlag.equalsIgnoreCase(xFor)) {
            xFor = httpServletRequest.getHeader("WL-Proxy-Client-IP");
        }
        if (StringUtils.isBlank(xFor) || unknownFlag.equalsIgnoreCase(xFor)) {
            xFor = httpServletRequest.getHeader("HTTP_CLIENT_IP");
        }
        if (StringUtils.isBlank(xFor) || unknownFlag.equalsIgnoreCase(xFor)) {
            xFor = httpServletRequest.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (StringUtils.isBlank(xFor) || unknownFlag.equalsIgnoreCase(xFor)) {
            xFor = httpServletRequest.getRemoteAddr();
        }
        return xFor;
    }

}
