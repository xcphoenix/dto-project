package com.xcphoenix.dto.bean.dao;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import java.sql.Timestamp;

/**
 *
 * @author      xuanc
 * @date        2019/12/4 下午3:59
 * @version     1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Comment {

    private static final String ANONYMOUS_NAME = "匿名用户";
    private static final String ANONYMOUS_AVATAR = "http://i.waimai.meituan.com/static/img/default-avatar.png";

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
    /**
     * 用户信息
     */
    @JSONField(deserialize = false)
    private String userName;
    @JSONField(deserialize = false)
    private String userAvatar;

    public void anonymousConvert() {
        this.userName = ANONYMOUS_NAME;
        this.userAvatar = ANONYMOUS_AVATAR;
    }

}
