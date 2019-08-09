package com.xcphoenix.dto.mapper;

import com.xcphoenix.dto.bean.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

/**
 * @author      xuanc
 * @date        2019/8/9 上午8:43
 * @version     1.0
 */ 
public interface UserMapper {

    /**
     * 检测用户是否已注册 - By Phone
     * @param phone 手机号
     * @return
     *   <li>用户 id</li>
     *   <li>null: 用户不存在</li>
     */
    @Select("SELECT user_id FROM user WHERE user_phone = #{phone} ")
    Integer isExistsByPhone(String phone);

    /**
     * 检测用户是否已注册 - By Name
     * @param username 用户名
     * @return
     *   <li>用户 id</li>
     *   <li>null: 用户不存在</li>
     */
    @Select("SELECT user_id FROM user WHERE user_name = #{username} ")
    Integer isExistsByName(String username);

    /**
     * 检测用户是否已开店
     * @param userId 用户 id
     * @return
     *         <li>用户所开的店铺id</li>
     *         <li>null: 用户没有用开店记录</li>
     */
    @Select("SELECT restaurant_id FROM restaurant WHERE user_id = #{userId} ")
    Integer hasRestaurant(String userId);

    /**
     * 使用用户名和密码登录
     * @param username 用户名
     * @param password 密码
     * @return
     *         <li>用户 user_id</li>
     *         <li>null: 用户不存在或密码错误</li>
     */
    @Select("SELECT user_id FROM user WHERE user_name = #{username} AND user_password = #{password}")
    Integer loginByName(String username, String password);

    /**
     * 使用用户名和密码登录
     * @param phone 手机号
     * @param password 密码
     * @return
     *         <li>用户 user_id</li>
     *         <li>null: 用户不存在或密码错误</li>
     */
    @Select("SELECT user_id FROM user WHERE user_phone = #{phone} AND user_password = #{password}")
    Integer loginByPhonePass(String phone, String password);

    /**
     * 使用手机号+密码注册用户
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
