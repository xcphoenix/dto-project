package com.xcphoenix.dto.bean.dao;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import java.sql.Timestamp;

/**
 * TODO 评价支持图片
 *
 * @author      xuanc
 * @date        2019/12/4 下午3:59
 * @version     1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Comment {

    private Long id;
    private Long replyId;
    private Long rstId;
    private Long userId;
    private Integer foodScore;
    private Integer deliveryScore;
    private Integer packageScore;
    @Length(max = 300, message = "备注长度超出限制")
    private String comment;
    private Timestamp time;
    private Boolean isAnonymous;
    private Boolean isShopper;

}
