package com.xcphoenix.dto.service.impl;

import com.xcphoenix.dto.bean.User;
import com.xcphoenix.dto.mapper.UserMapper;
import com.xcphoenix.dto.service.UserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author      xuanc
 * @date        2019/8/9 上午10:57
 * @version     1.0
 */
@Service
public class UserServiceImpl implements UserService {

    @Resource
    private UserMapper userMapper;

    @Override
    public Boolean isExists(String str) {
        char firstCh = str.charAt(0);
        if (Character.isDigit(firstCh)) {
            return userMapper.isExistsByPhone(str) != null;
        } else {
            return userMapper.isExistsByName(str) != null;
        }
    }

    @Override
    public Integer loginByPhonePass(String phone, String password) {
        return userMapper.loginByPhonePass(phone, password);
    }

    @Override
    public Integer loginByName(String username, String password) {
        return userMapper.loginByName(username, password);
    }

    @Override
    public Integer registerByPhonePass(User user) {
         if(userMapper.isExistsByPhone(user.getUserPhone()) != null) {
             return null;
         }
         userMapper.registerByPhonePass(user);
         return user.getUserId();
    }
}
