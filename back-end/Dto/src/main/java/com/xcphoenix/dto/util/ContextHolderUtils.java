package com.xcphoenix.dto.util;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

/**
 * 获取请求的 request
 * @author xuanc
 * @version 1.0
 * @date 2019/8/14 下午8:53
 */
public class ContextHolderUtils {

    public static HttpServletRequest getRequest() {
        return ((ServletRequestAttributes) Objects.
                requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
    }

    /**
     * 获取登录用户 id
     */
    public static Integer getLoginUserId() {
        Object object =  getRequest().getAttribute("userId");
        if (!(object instanceof Integer)) {
            throw new RuntimeException("用户id不存在");
        }
        return (Integer) object;
    }

}

