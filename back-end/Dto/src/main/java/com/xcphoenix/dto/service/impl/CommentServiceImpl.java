package com.xcphoenix.dto.service.impl;

import com.github.pagehelper.PageHelper;
import com.xcphoenix.dto.bean.dao.Comment;
import com.xcphoenix.dto.bean.dto.PageObject;
import com.xcphoenix.dto.mapper.CommentMapper;
import com.xcphoenix.dto.service.CommentService;
import com.xcphoenix.dto.service.UserService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 *
 * @author      xuanc
 * @date        2019/12/6 下午8:48
 * @version     1.0
 */
@Service
public class CommentServiceImpl implements CommentService {

    private UserService userService;
    private CommentMapper commentMapper;

    public CommentServiceImpl(UserService userService, CommentMapper commentMapper) {
        this.userService = userService;
        this.commentMapper = commentMapper;
    }

    @Override
    public void addComment(Comment comment) {
        commentMapper.addComment(comment);
    }

    @Override
    public List<Comment> getCommitByUser(Long userId, int from, int size) {
        PageHelper.offsetPage(from, PageObject.properPageSize(size));
        return null;
    }

    @Override
    public List<Comment> getCommitByRst(Long rstId, int from, int size) {
        PageHelper.offsetPage(from, PageObject.properPageSize(size));
        List<Comment> comments = commentMapper.getCommentByRstId(rstId);
        // 处理匿名用户
        comments.forEach(comment -> {
            if (comment.getIsAnonymous()) {
                comment.anonymousConvert();
            }
        });
        return comments;
    }

}
