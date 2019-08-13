package com.xcphoenix.dto.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.xcphoenix.dto.annotation.PassToken;
import com.xcphoenix.dto.annotation.UserLoginToken;
import com.xcphoenix.dto.bean.User;
import com.xcphoenix.dto.result.ErrorCode;
import com.xcphoenix.dto.service.TokenService;
import com.xcphoenix.dto.service.LoginService;
import com.xcphoenix.dto.result.Result;
import com.xcphoenix.dto.service.UserService;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @author      xuanc
 * @date        2019/8/9 下午3:14
 * @version     1.0
 */
@RestController
public class LoginController {

    private LoginService loginService;
    private TokenService tokenService;
    private UserService userService;

    @Autowired
    public LoginController(LoginService loginService, TokenService tokenService, UserService userService) {
        this.loginService = loginService;
        this.tokenService = tokenService;
        this.userService = userService;
    }

    @PassToken
    @PostMapping("/login/login_phone_pass")
    public Result loginByPhonePass(@RequestBody JSONObject jsonObject) {
        String phone = jsonObject.getString("userPhone");
        String passwd = jsonObject.getString("userPassword");
        if (!loginService.isExists(phone)) {
            return Result.error(ErrorCode.MOBILE_NOT_FOUND);
        }
        Integer userId = loginService.loginByPhonePass(phone, passwd);
        if (userId == null) {
            return Result.error(ErrorCode.LOGIN_PASSWD_ERROR);
        }

        User userDetail = userService.getUserDetail(userId);

        String token = tokenService.createToken(userDetail);
        Map<String, Object> data = new HashMap<>(1);
        data.put("token", token);
        data.put("id", userId);
        data.put("timestamp", System.currentTimeMillis());
        return new Result(200, "登录成功", data);
    }

    @PassToken
    @PostMapping("/login/login_name_pass")
    public Result loginByNamePass(@RequestBody JSONObject jsonObject) {
        String userName = jsonObject.getString("userName");
        String userPassword = jsonObject.getString("userPassword");

        if (!loginService.isExists(userName)) {
            return Result.error(ErrorCode.USER_NOT_FOUND);
        }
        Integer userId = loginService.loginByName(userName, userPassword);
        if (userId == null) {
            return Result.error(ErrorCode.LOGIN_PASSWD_ERROR);
        }
        User userDetail = userService.getUserDetail(userId);

        String token = tokenService.createToken(userDetail);
        Map<String, Object> data = new HashMap<>(1);
        data.put("token", token);
        data.put("id", userId);
        data.put("timestamp", System.currentTimeMillis());
        return new Result(200, "登录成功", data);
    }

    @PassToken
    @PostMapping("/register")
    public Result registerTmpDev(@Validated @RequestBody User user) {
        user.setUserName(RandomStringUtils.randomAlphanumeric(2) + System.currentTimeMillis());
        Integer userId = loginService.registerByPhonePass(user);
        if (userId == null) {
            return Result.error(ErrorCode.MOBILE_REGISTERED);
        }
        User userDetail = userService.getUserDetail(userId);

        Map<String, Object> data = new HashMap<>(5);
        data.put("token", tokenService.createToken(userDetail));
        data.put("id", userId);
        data.put("name", user.getUserName());
        return new Result(200, "注册成功", data);
    }

    @UserLoginToken
    @GetMapping("/logout")
    public Result logout(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        Integer userId = tokenService.getUserId(token);
        tokenService.putBlacklist(userId, token);
        return new Result(200, "注销成功", null);
    }

    /**
     * test ...
     */
    @UserLoginToken
    @GetMapping("/test")
    public Result testToken(HttpServletRequest request) {
        return new Result(200, "success",
                Arrays.asList(request.getAttribute("userId"), request.getAttribute("userStatus")));
    }

}
