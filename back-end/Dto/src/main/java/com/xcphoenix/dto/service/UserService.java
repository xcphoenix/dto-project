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
     * 获取用户状态
     *
     * @return 用户状态：0：普通用户　1: 商家 2: 客服 3：运营 4：财务　5：root
     */
    Integer getUserStatus();

    /**
     * 更新用户状态为商家
     */
    void becomeShopper();

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
     * @return 头像url
     * @throws IOException ..
     */
    String updateAvatar(String avatar) throws IOException;

}
