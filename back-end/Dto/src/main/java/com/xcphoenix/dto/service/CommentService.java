package com.xcphoenix.dto.service;

import com.xcphoenix.dto.bean.dao.Comment;

/**
 * @author      xuanc
 * @date        2019/12/6 下午8:48
 * @version     1.0
 */ 
public interface CommentService {

    /**
     * 添加评论
     *
     * @param comment 评论数据
     */
    void addComment(Comment comment);



}
