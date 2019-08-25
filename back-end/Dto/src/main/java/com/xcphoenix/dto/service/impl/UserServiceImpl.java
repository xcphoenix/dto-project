package com.xcphoenix.dto.service.impl;

import com.xcphoenix.dto.bean.User;
import com.xcphoenix.dto.exception.ServiceLogicException;
import com.xcphoenix.dto.mapper.UserMapper;
import com.xcphoenix.dto.result.ErrorCode;
import com.xcphoenix.dto.service.Base64ImgService;
import com.xcphoenix.dto.service.UserService;
import com.xcphoenix.dto.util.ContextHolderUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * @author      xuanc
 * @date        2019/8/12 下午10:04
 * @version     1.0
 */
@Service
public class UserServiceImpl implements UserService {

    private UserMapper userMapper;
    private Base64ImgService base64ImgService;
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Value("${upload.image.directory.avatar}")
    private String avatarDire;

    @Autowired
    public UserServiceImpl(UserMapper userMapper, Base64ImgService base64ImgService) {
        this.userMapper = userMapper;
        this.base64ImgService = base64ImgService;
    }

    @Override
    public User getUserDetail(Integer userId) {
        return userMapper.getUserDetail(userId);
    }

    @Override
    public void updateName(String name) {
        Integer userId = ContextHolderUtils.getLoginUserId();
        try {
            userMapper.updateNameById(userId, name);
        } catch (DuplicateKeyException dke) {
            logger.warn("用户名冲突", dke);
            throw new ServiceLogicException(ErrorCode.NAME_NOT_UNIQUE);
        }
    }

    @Override
    public String updateAvatar(String avatar) throws IOException {
        Integer userId = ContextHolderUtils.getLoginUserId();
        String avatarUrl = base64ImgService.convertPicture(avatar, avatarDire);
        userMapper.updateAvatarById(userId, avatarUrl);
        return avatarUrl;
    }
}
