package com.xcphoenix.dto.service.impl;

import com.xcphoenix.dto.mapper.CommentMapper;
import com.xcphoenix.dto.service.UserService;
import org.springframework.stereotype.Service;

/**
 * TODO redis 缓存数据
 *
 * @author      xuanc
 * @date        2019/12/6 下午8:48
 * @version     1.0
 */
@Service
public class CommentServiceImpl {

    private UserService userService;
    private CommentMapper commentMapper;

    public CommentServiceImpl(UserService userService, CommentMapper commentMapper) {
        this.userService = userService;
        this.commentMapper = commentMapper;
    }

    public void addComment() {

    }


}
