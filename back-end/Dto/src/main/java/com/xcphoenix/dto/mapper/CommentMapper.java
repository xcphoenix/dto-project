package com.xcphoenix.dto.mapper;

import com.xcphoenix.dto.bean.dao.Comment;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author xuanc
 * @version 1.0
 * @date 2019/12/4 下午10:22
 */
public interface CommentMapper {

    /**
     * 添加评论
     *
     * @param comment 评论信息
     */
    void addComment(Comment comment);

    /**
     * 获得店铺的评论（不包括评论回复）
     *
     * @param rstId 店铺id
     * @return 店铺评论的部分信息
     */
    List<Comment> getCommentByRstId(@Param("rst") Long rstId);

    /**
     * 获取评论的回复
     * <strong>评论的回复目前只展示商家回复</strong>
     *
     * @param commentId 评论id
     * @return 回复list
     */
    List<Comment> getReplyByComment(@Param("commentId") Long commentId);

}
