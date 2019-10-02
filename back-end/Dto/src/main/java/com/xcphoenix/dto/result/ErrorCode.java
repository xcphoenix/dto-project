package com.xcphoenix.dto.result;

import lombok.Data;

/**
 * @author xuanc
 * @version 1.0
 * @date 2019/8/9 上午11:01
 */
@Data
public class ErrorCode {

    /**
     * 序列化 id
     */
    private static final long serialVersionUID = 111111111111L;

    private int code;
    private String msg;

    /**
     * 私有
     */
    private ErrorCode(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public ErrorCode setErrorMsg(String errorMsg) {
        this.msg = errorMsg;
        return this;
    }

    /*
     * 默认异常处理 ---------------------------------
     */

    /**
     * 非法参数
     */
    public static ErrorCode illegalArgumentBuilder() {
        String defaultMsg = "无效的参数";
        return illegalArgumentBuilder(defaultMsg);
    }
    public static ErrorCode illegalArgumentBuilder(String msg) {
        return new ErrorCode(4001, msg);
    }

    /**
     * 默认异常
     */
    public static final ErrorCode SERVER_EXCEPTION = new ErrorCode(4000, "服务器异常");

    /*
     * 业务逻辑相关 ----------------------------------
     */

    /**
     *  token 相关 10001 ~ 10005
     */
    public static final ErrorCode TOKEN_NOT_FOUND = new ErrorCode(10001, "找不到token");
    public static final ErrorCode TOKEN_TIME_EXPIRED = new ErrorCode(10002, "token已过期");
    public static final ErrorCode TOKEN_DECODED_ERROR = new ErrorCode(10003, "token解析错误");
    public static final ErrorCode TOKEN_DECODED_REFRESH = new ErrorCode(10004, "token已刷新");
    public static final ErrorCode TOKEN_INVALID = new ErrorCode(10005, "无效的token");

    /**
     * 用户相关 20001 ~
     */
    public static final ErrorCode MOBILE_NOT_FOUND = new ErrorCode(20001, "手机号不存在");
    public static final ErrorCode USER_NOT_FOUND = new ErrorCode(20002, "用户不存在");
    public static final ErrorCode LOGIN_PASSWD_ERROR = new ErrorCode(20003, "密码错误");
    public static final ErrorCode MOBILE_REGISTERED = new ErrorCode(20004, "手机号已注册");
    public static final ErrorCode NAME_NOT_UNIQUE = new ErrorCode(20005, "用户名已被占用");
    public static final ErrorCode USER_NOT_LOGIN = new ErrorCode(20006, "未登录");

    /**
     * 文件相关
     */
    public static final ErrorCode FILE_NOT_IMAGE = new ErrorCode(30001, "不是有效的图片文件");
    public static final ErrorCode FILE_SIZE_OUT_OF_RANGE = new ErrorCode(30002, "图片文件大小超出范围");
    public static final ErrorCode BASE64_IS_NULL = new ErrorCode(30003, "图片参数不存在");

    /**
     * 店铺相关
     */
    public static final ErrorCode USER_HAVE_SHOP = new ErrorCode(40001, "用户只能添加一个店铺");
    public static final ErrorCode USER_NOT_SHOPPER = new ErrorCode(40002, "用户不是商家");
    public static final ErrorCode SHOP_NOT_FOUND = new ErrorCode(40003, "店铺不存在");

    /**
     * 商品相关
     */
    public static final ErrorCode CATEGORY_DUPLICATE = new ErrorCode(50001, "食品分类重复");
    public static final ErrorCode CATEGORY_NOT_FOUND = new ErrorCode(50002, "食品分类不存在");
    public static final ErrorCode FOOD_NAME_DUPLICATE = new ErrorCode(50003, "食品名已被占用");
    public static final ErrorCode FOOD_NOT_FOUND = new ErrorCode(50004, "食品不存在");
    public static final ErrorCode CATEGORY_NAME_CONFLICT = new ErrorCode(50005, "不允许的分类名");

    /**
     * 收货地址
     */
    public static final ErrorCode ADDR_NOT_FOUND = new ErrorCode(60001, "收货地址不存在");

    /**
     * 收藏相关
     */
    public static final ErrorCode SHOP_HAVE_COLLECTED = new ErrorCode(70001, "店铺已经收藏过了");
    public static final ErrorCode SHOP_NOT_COLLECTED = new ErrorCode(70002, "未收藏该店铺");

}
