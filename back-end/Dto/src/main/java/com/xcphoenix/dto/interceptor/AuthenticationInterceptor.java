package com.xcphoenix.dto.interceptor;

import com.alibaba.fastjson.JSON;
import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.xcphoenix.dto.annotation.PassToken;
import com.xcphoenix.dto.annotation.UserLoginToken;
import com.xcphoenix.dto.bean.User;
import com.xcphoenix.dto.exception.ServiceLogicException;
import com.xcphoenix.dto.result.ErrorCode;
import com.xcphoenix.dto.result.Result;
import com.xcphoenix.dto.service.TokenService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.Date;

/**
 * 拦截器 - 验证 token
 * @author      xuanc
 * @date        2019/8/2 下午4:26
 * @version     1.0
 */
public class AuthenticationInterceptor implements HandlerInterceptor {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private TokenService tokenService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 获取 token
        String token = request.getHeader("Authorization");

        // 如果不是映射到方法直接通过
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }

        HandlerMethod handlerMethod = (HandlerMethod)handler;
        Method method = handlerMethod.getMethod();

        // 检查是否有 @PassToken 注解
        if (method.isAnnotationPresent(PassToken.class)) {
            PassToken passToken = method.getAnnotation(PassToken.class);
            return passToken.required();
        }

        // 检查有没有需要用户权限的注解
        if (method.isAnnotationPresent(UserLoginToken.class)) {
            UserLoginToken userLoginToken = method.getAnnotation(UserLoginToken.class);
            if (userLoginToken.required()) {
                // 执行认证
                if (token == null) {
                    throw new ServiceLogicException(ErrorCode.TOKEN_NOT_FOUND);
                }

                int userId, userStatus;
                Date expireTime;
                Date refreshTime;
                try {
                    userId = Integer.parseInt(JWT.decode(token).getAudience().get(0)) ;
                    expireTime = JWT.decode(token).getExpiresAt();
                    refreshTime = JWT.decode(token).getClaim("refresh").asDate();
                    userStatus = JWT.decode(token).getClaim("status").asInt();
                } catch (JWTDecodeException | ClassCastException | NumberFormatException ex) {
                    ex.printStackTrace();
                    throw new ServiceLogicException(ErrorCode.TOKEN_DECODED_ERROR);
                }

                request.setAttribute("userId", userId);
                request.setAttribute("userStatus", userStatus);

                if (expireTime == null || refreshTime == null) {
                    throw new ServiceLogicException(ErrorCode.TOKEN_INVALID);
                }

                if (refreshTime.getTime() < System.currentTimeMillis()) {
                    throw new ServiceLogicException(ErrorCode.TOKEN_TIME_EXPIRED);
                } else if (tokenService.verifierToken(token) && tokenService.checkInBlacklist(userId, token)) {
                    if (expireTime.getTime() < System.currentTimeMillis()) {
                        User userDetail = new User();
                        userDetail.setUserId(userId);
                        userDetail.setStatus(userStatus);

                        // 创建新 token
                        String newToken = tokenService.createToken(userDetail);
                        response.setContentType("application/json;charset=UTF-8");
                        PrintWriter out = response.getWriter();
                        Result result = Result.error(ErrorCode.TOKEN_DECODED_REFRESH)
                                .addMap("newToken", newToken);
                        out.write(JSON.toJSONString(result));
                        out.flush();
                        out.close();
                        return true;
                    }
                } else {
                    throw new ServiceLogicException(ErrorCode.TOKEN_INVALID);
                }
                return true;
            }
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) throws Exception {
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
                                Exception ex) throws Exception {
    }
}
