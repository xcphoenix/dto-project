package com.xcphoenix.dto.controller;

import com.alibaba.fastjson.JSONObject;
import com.xcphoenix.dto.annotation.PassToken;
import com.xcphoenix.dto.annotation.UserLoginToken;
import com.xcphoenix.dto.bean.User;
import com.xcphoenix.dto.result.ErrorCode;
import com.xcphoenix.dto.result.Result;
import com.xcphoenix.dto.service.LoginService;
import com.xcphoenix.dto.service.TokenService;
import com.xcphoenix.dto.service.UserService;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @author xuanc
 * @version 1.0
 * @date 2019/8/9 下午3:14
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

        return new Result("登录成功")
                .addMap("token", token)
                .addMap("id", userId)
                .addMap("timestamp", System.currentTimeMillis());
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

        return new Result("登录成功")
                .addMap("token", token)
                .addMap("id", userId)
                .addMap("timestamp", System.currentTimeMillis());
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

        return new Result("登录成功")
                .addMap("token", tokenService.createToken(userDetail))
                .addMap("id", userId)
                .addMap("timestamp", System.currentTimeMillis());
    }

    @UserLoginToken
    @GetMapping("/logout")
    public Result logout(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        Integer userId = tokenService.getUserId(token);
        tokenService.putBlacklist(userId, token);
        return new Result("注销成功");
    }


}
