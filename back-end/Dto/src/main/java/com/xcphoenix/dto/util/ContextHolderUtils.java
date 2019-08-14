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

}

