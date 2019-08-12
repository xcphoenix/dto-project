package com.xcphoenix.dto.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.xcphoenix.dto.exception.ServiceLogicException;
import com.xcphoenix.dto.result.ErrorCode;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @author      xuanc
 * @date        2019/8/7 下午3:24
 * @version     1.0
 */
@Service
@PropertySource("classpath:token.properties")
public class TokenService {

    private final String secret = "Let life be beautiful like summer flowers and death like autumn leaves...";

    @Value("#{${token.expire.time} * 60 * 1000 * (${token.time.unit.minute} ? 1 : 60)}")
    private long expireTime;
    @Value("#{${token.refresh.time} * 60 * 1000 * (${token.time.unit.minute} ? 1 : 60)}")
    private long refreshTime;

    @Resource
    private RedisTemplate redisTemplate;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    public String createToken(int userId) {
        String iss = "elmqx.xcphoenix.com";
        Date exp = new Date(System.currentTimeMillis() + expireTime);
        Date refresh = new Date(System.currentTimeMillis() + refreshTime);
        String tmpStr = UUID.randomUUID().toString().replace("-", "")
                + new Random().nextLong();
        String jti = new String(Hex.encodeHex(DigestUtils.md5(tmpStr)));
        return JWT.create().withAudience(String.valueOf(userId))
                .withExpiresAt(exp)
                .withJWTId(jti)
                .withIssuer(iss)
                .withClaim("refresh", refresh)
                .sign(Algorithm.HMAC256(secret));
    }

    public Boolean verifierToken(String token)  {
        JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(secret))
                .build();
        try {
            jwtVerifier.verify(token);
        } catch (JWTVerificationException jve) {
            throw new ServiceLogicException(ErrorCode.TOKEN_INVALID);
        }
        return true;
    }

    public void putBlacklist(int userId, String token) {
        stringRedisTemplate.opsForValue().set("blacklist:" + userId + ":token", token, refreshTime, TimeUnit.MILLISECONDS);
    }

    public void putPreBlacklist(int userId) {
        stringRedisTemplate.opsForValue().set("preBlacklist:" + userId, String.valueOf(System.currentTimeMillis()),
                refreshTime, TimeUnit.MILLISECONDS);
    }

    public boolean checkInBlacklist(int userId, String token) {
        String tmpTime = stringRedisTemplate.opsForValue().get("preBlacklist:" + userId);
        if (tmpTime != null) {
            long logoutServerTime = Long.parseLong(tmpTime);
            if (System.currentTimeMillis() < logoutServerTime) {
                return false;
            }
        }
        String tmpToken = stringRedisTemplate.opsForValue().get("blacklist:" + userId + ":token");
        if (tmpToken != null) {
            return !tmpToken.equals(token);
        }
        return true;
    }

    public Integer getUserId(String token) {
        return Integer.parseInt(JWT.decode(token).getAudience().get(0)) ;
    }

}
