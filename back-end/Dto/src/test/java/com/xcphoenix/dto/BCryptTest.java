package com.xcphoenix.dto;

import at.favre.lib.crypto.bcrypt.BCrypt;
import com.xcphoenix.dto.service.EncryptService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author xuanc
 * @version 1.0
 * @date 2020/2/28 下午6:09
 */
@SpringBootTest
public class BCryptTest {

    @Autowired
    private EncryptService encryptService;

    @Test
    void testBcrypt() {
        String testPasswd = "098f6bcd4621d373cade4e832627b4f6";
        BCrypt.Hasher hasher = BCrypt.withDefaults();
        String ps = new String(hasher.hash(10, testPasswd.getBytes()));
        System.out.println((encryptService.encryptPasswd(testPasswd)));
        Assertions.assertTrue(BCrypt.verifyer().verify(testPasswd.getBytes(), ps.getBytes()).verified);
    }

}
