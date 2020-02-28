package com.xcphoenix.dto.mapper;

import com.xcphoenix.dto.bean.dao.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * @author xuanc
 * @version 1.0
 * @date 2019/8/9 上午8:43
 */
public interface LoginMapper {

    /**
     * 检测用户是否已注册 - By Phone
     *
     * @param phone 手机号
     * @return <li>用户 id</li>
     * <li>null: 用户不存在</li>
     */
    @Select("SELECT user_id FROM user WHERE user_phone = #{phone} ")
    Integer isExistsByPhone(@Param("phone") String phone);

    /**
     * 检测用户是否已注册 - By Name
     *
     * @param username 用户名
     * @return <li>用户 id</li>
     * <li>null: 用户不存在</li>
     */
    @Select("SELECT user_id FROM user WHERE user_name = #{username} ")
    Integer isExistsByName(@Param("username") String username);

    /**
     * 使用用户名和密码登录
     *
     * @param username 用户名
     * @return <li>用户 user_id</li>
     * <li>null: 用户不存在或密码错误</li>
     */
    @Select("SELECT user_id, user_password FROM user WHERE user_name = #{username}")
    User loginByName(@Param("username") String username);

    /**
     * 使用用户名和密码登录
     *
     * @param phone    手机号
     * @return <li>用户 user_id</li>
     * <li>null: 用户不存在或密码错误</li>
     */
    @Select("SELECT user_id, user_password FROM user WHERE user_phone = #{phone}")
    User loginByPhonePass(@Param("phone") String phone);

    /**
     * 使用手机号+密码注册用户
     *
     * @param user 用户信息<br />
     *             <strong>userName 使用随机两位英文+时间戳</strong>
     */
    @Options(useGeneratedKeys = true, keyProperty = "userId")
    @Insert("INSERT INTO user(" +
            "user_name, user_phone, user_password, is_set_passwd, gmt_create) " +
            "VALUES (" +
            "#{userName}, #{userPhone} , #{userPassword} , TRUE, NOW() )")
    void registerByPhonePass(User user);

}
