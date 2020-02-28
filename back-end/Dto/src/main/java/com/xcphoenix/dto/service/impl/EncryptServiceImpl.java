package com.xcphoenix.dto.service.impl;

import at.favre.lib.crypto.bcrypt.BCrypt;
import com.xcphoenix.dto.service.EncryptService;
import org.springframework.stereotype.Service;

/**
 * @author      xuanc
 * @date        2020/2/28 下午7:50
 * @version     1.0
 */
@Service
public class EncryptServiceImpl implements EncryptService {

    private int hashCost = 10;
    private BCrypt.Hasher hasher = BCrypt.withDefaults();
    private BCrypt.Verifyer verifyer = BCrypt.verifyer();

    @Override
    public String encryptPasswd(String passwd) {
        return hasher.hashToString(hashCost, passwd.toCharArray());
    }

    @Override
    public boolean validatePasswd(String passwd, String hashPasswd) {
        return verifyer.verify(passwd.getBytes(), hashPasswd.getBytes()).verified;
    }

}
