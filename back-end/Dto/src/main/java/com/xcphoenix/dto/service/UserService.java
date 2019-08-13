package com.xcphoenix.dto.service;

import com.xcphoenix.dto.bean.User;

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

}
