package com.xcphoenix.dto.bean.dto;

import com.xcphoenix.dto.bean.dao.Comment;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * TODO anonymous avatar url 数据配置化
 *
 * @author      xuanc
 * @date        2019/12/4 下午10:39
 * @version     1.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentData extends Comment {

    private static final String ANONYMOUS = "匿名用户";
    private static final String SHOPPER_NICK = "商家回复";
    private static final String ANONYMOUS_AVATAR_URL = "comment/avatar/anonymous.png";

    private String nick;
    private String avatarUrl;
    private String timeDesc;
    private Integer rate;
    private String rateDesc;
    private Comment replay;

    /**
     * 根据角色：匿名用户、普通用户、商家
     * 判定属性：
     *  - 昵称(商家默认/匿名：匿名用户)
     *  - 头像(url：用户头像url 或 默认url)(商家无)
     *  - 时间显示(商家不显示时间)
     *  - 评分(商家无)
     *  - 评分描述(商家无)
     */
    private void encryptAsRole() {
        if (this.getIsShopper()) {
            this.nick = SHOPPER_NICK;
        } else if (this.getIsAnonymous()) {
            this.nick = ANONYMOUS;
        } else {
            // get user nick
        }
    }

}
