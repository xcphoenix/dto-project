package com.xcphoenix.dto.service.impl;

import com.xcphoenix.dto.bean.User;
import com.xcphoenix.dto.mapper.UserMapper;
import com.xcphoenix.dto.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author      xuanc
 * @date        2019/8/12 下午10:04
 * @version     1.0
 */
@Service
public class UserServiceImpl implements UserService {

    private UserMapper userMapper;

    @Autowired
    public UserServiceImpl(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @Override
    public User getUserDetail(Integer userId) {
        return userMapper.getUserDetail(userId);
    }
}
