package com.xcphoenix.dto.mapper;

import com.xcphoenix.dto.bean.User;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

/**
 * @author xuanc
 * @version 1.0
 * @date 2019/8/12 下午9:58
 */
public interface UserMapper {

    /**
     * 获取用户信息
     *
     * @param userId 用户id
     * @return user detail
     */
    @Select("SELECT user_id, user_name, user_phone, user_avatar, user_sex, user_status " +
            "FROM user WHERE user_id = #{userId}")
    User getUserDetail(Integer userId);

    /**
     * 获取用户状态
     *
     * @param userId 用户id
     * @return 用户状态
     */
    @Select("SELECT user_status FROM user WHERE user_id = #{userId} ")
    Integer getUserStatus(Integer userId);

    /**
     * 更新用户身份为商家
     *
     * @param userId 用户id
     */
    @Update("UPDATE user SET user_status = 1 WHERE user_id = #{userId} ")
    void becomeShopper(Integer userId);

    /**
     * 更新用户名
     *
     * @param userId   用户id
     * @param username 用户名
     */
    @Update("UPDATE user SET user_name = #{username} WHERE user_id = #{userId} ")
    void updateNameById(@Param("userId") Integer userId, @Param("username") String username);

    /**
     * 更新用户头像
     *
     * @param userId 用户
     * @param avatarUrl 用户头像
     */
    @Update("UPDATE user SET user_avatar = #{avatarUrl} WHERE user_id = #{userId} ")
    void updateAvatarById(@Param("userId") Integer userId, @Param("avatarUrl") String avatarUrl);


}
