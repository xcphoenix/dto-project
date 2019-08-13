package com.xcphoenix.dto.mapper;

import com.xcphoenix.dto.bean.User;
import org.apache.ibatis.annotations.Select;

/**
 * @author xuanc
 * @version 1.0
 * @date 2019/8/12 下午9:58
 */
public interface UserMapper {

    /**
     * 获取用户信息
     * @param userId 用户id
     * @return user detail
     */
    @Select("SELECT user_id, user_name, user_phone, user_avatar, user_sex, user_status " +
            "FROM user WHERE user_id = #{userId}")
    User getUserDetail(Integer userId);

}
