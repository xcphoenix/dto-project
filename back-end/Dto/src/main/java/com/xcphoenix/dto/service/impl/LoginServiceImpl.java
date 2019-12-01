package com.xcphoenix.dto.service.impl;

import com.xcphoenix.dto.bean.dao.User;
import com.xcphoenix.dto.exception.ServiceLogicException;
import com.xcphoenix.dto.mapper.LoginMapper;
import com.xcphoenix.dto.result.ErrorCode;
import com.xcphoenix.dto.service.LoginService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author      xuanc
 * @date        2019/8/9 上午10:57
 * @version     1.0
 */
@Service
public class LoginServiceImpl implements LoginService {

    @Resource
    private LoginMapper loginMapper;

    @Override
    public Boolean isExists(String str) {
        if (str == null) {
            throw new ServiceLogicException(ErrorCode.illegalArgumentBuilder("缺少必要的参数"));
        }
        char firstCh = str.charAt(0);
        if (Character.isDigit(firstCh)) {
            return loginMapper.isExistsByPhone(str) != null;
        } else {
            return loginMapper.isExistsByName(str) != null;
        }
    }

    @Override
    public Long loginByPhonePass(String phone, String password) {
        if (phone == null || password == null) {
            throw new ServiceLogicException(ErrorCode.illegalArgumentBuilder("缺少必要的参数"));
        }
        return loginMapper.loginByPhonePass(phone, password);
    }

    @Override
    public Long loginByName(String username, String password) {
        if (username == null || password == null) {
            throw new ServiceLogicException(ErrorCode.illegalArgumentBuilder("缺少必要的参数"));
        }
        return loginMapper.loginByName(username, password);
    }

    @Override
    public Long registerByPhonePass(User user) {
         if(loginMapper.isExistsByPhone(user.getUserPhone()) != null) {
             return null;
         }
         loginMapper.registerByPhonePass(user);
         return user.getUserId();
    }
}
