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
     * JSR 参数错误
     */
    public static final ErrorCode BIND_EXCEPTION = new ErrorCode(4001, null);
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

    /**
     * 文件相关
     */
    public static final ErrorCode FILE_NOT_IMAGE = new ErrorCode(30001, "不是有效的图片文件");
    public static final ErrorCode FILE_SIZE_OUT_OF_RANGE = new ErrorCode(30002, "图片文件大小超出范围");

}
