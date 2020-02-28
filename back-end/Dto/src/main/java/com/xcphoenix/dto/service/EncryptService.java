package com.xcphoenix.dto.service;

/**
 * @author      xuanc
 * @date        2020/2/28 下午7:46
 * @version     1.0
 */ 
public interface EncryptService {

    /**
     * 对密码再次加密
     *
     * @param passwd 密码
     * @return 加密后的数据
     */
    String encryptPasswd(String passwd);

    /**
     * 校验密码
     *
     * @param passwd 密码
     * @param hashPasswd hash 后的密码
     * @return 校验是否成功
     */
    public boolean validatePasswd(String passwd, String hashPasswd);
}
