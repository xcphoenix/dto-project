package com.xcphoenix.dto.result;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author      xuanc
 * @date        2019/8/8 下午6:18
 * @version     1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result {

    private int code = 200;
    private String msg;
    private Object data;

    public Result(String msg, Object data) {
        this.msg = msg;
        this.data = data;
    }

    private Result(ErrorCode errorCode) {
        this.code = errorCode.getCode();
        this.msg = errorCode.getMsg();
        this.data = null;
    }

    public static Result error(ErrorCode errorCode) {
        return new Result(errorCode);
    }
}
