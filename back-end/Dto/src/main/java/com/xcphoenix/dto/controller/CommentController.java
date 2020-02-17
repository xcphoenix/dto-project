package com.xcphoenix.dto.controller;

import com.xcphoenix.dto.annotation.PassToken;
import com.xcphoenix.dto.annotation.UserLoginToken;
import com.xcphoenix.dto.bean.dao.Comment;
import com.xcphoenix.dto.result.Result;
import com.xcphoenix.dto.service.CommentService;
import com.xcphoenix.dto.utils.ContextHolderUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author      xuanc
 * @date        2020/2/17 下午3:28
 * @version     1.0
 */
@RestController
@RequestMapping("/comment")
public class CommentController {

    private CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @UserLoginToken
    @PostMapping("/rst/{rstId}")
    public Result addComment(@PathVariable("rstId") Long rstId,
                             @RequestBody Comment comment) {
        comment.setRstId(rstId);
        comment.setUserId(ContextHolderUtils.getLoginUserId());
        commentService.addComment(comment);
        return new Result("评论成功");
    }

    @PassToken
    @GetMapping("/rst/{rstId}")
    public Result getRstComment(@PathVariable("rstId") Long rstId,
                                @RequestParam("from") int from,
                                @RequestParam("size") int size) {
        List<Comment> comments = commentService.getCommitByRst(rstId, from, size);
        return new Result("查询成功").addMap("comments", comments);
    }

}
