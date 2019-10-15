package com.xcphoenix.dto.service;

import com.xcphoenix.dto.bean.User;

/**
 * @author xuanc
 * @version 1.0
 * @date 2019/8/9 上午8:42
 */
public interface LoginService {

    /**
     * 判断用户是否存在
     * @param str phone or name
     * @return 是否存在
     */
    Boolean isExists(String str);

    /**
     * 手机号+密码登录
     *
     * @param phone    手机号
     * @param password 用户密码
     * @return <li>用户 id</li>
     * <li>null：用户不存在</li>
     */
    Long loginByPhonePass(String phone, String password);

    /**
     * 手机号+密码登录
     *
     * @param username 用户名
     * @param password 用户密码
     * @return <li>用户 id</li>
     * <li>null：用户不存在</li>
     */
    Long loginByName(String username, String password);

    /**
     * 手机号+验证码登录
     * <u>暂未上线</u>
     *
     * @param phone 手机号
     * @param code  验证码
     * @return <li>用户 id</li>
     * <li>null：用户不存在</li>
     */
    default Integer loginByPhoneCode(String phone, String code) {
        return null;
    }

    /**
     * 手机号＋密码注册
     * @param user 用户信息
     * @return 用户 id
     */
    Long registerByPhonePass(User user);

}
