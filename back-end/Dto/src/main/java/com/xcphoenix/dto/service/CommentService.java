package com.xcphoenix.dto.service;

import com.xcphoenix.dto.bean.dao.Comment;

import java.util.List;

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

    /**
     * 获取用户评价
     *
     * @param userId 用户id
     * @param from 分页游标
     * @param size 条数
     * @return 用户评价
     */
    List<Comment> getCommitByUser(Long userId, int from, int size);

    /**
     * 获取店铺评价
     *
     * @param rstId 店铺id
     * @param from 分页游标
     * @param size 条数
     * @return 店铺评价
     */
    List<Comment> getCommitByRst(Long rstId, int from, int size);

}
