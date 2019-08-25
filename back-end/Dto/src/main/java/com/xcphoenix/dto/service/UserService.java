package com.xcphoenix.dto.service;

import com.xcphoenix.dto.bean.User;

import java.io.IOException;

/**
 * @author xuanc
 * @version 1.0
 * @date 2019/8/12 下午9:58
 */
public interface UserService {

    /**
     * 获取用户信息
     * @param userId 用户 id
     * @return 用户信息
     */
    User getUserDetail(Integer userId);

    /**
     * 更新用户名
     *
     * @param name 用户名
     */
    void updateName(String name);

    /**
     * 更新用户头像
     *
     * @param avatar 头像
     * @return
     */
    String updateAvatar(String avatar) throws IOException;

}
