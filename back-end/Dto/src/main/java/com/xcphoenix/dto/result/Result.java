package com.xcphoenix.dto.result;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

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
    private Map<String, Object> data;

    public Result(String msg) {
        this.msg = msg;
    }

    public Result(String msg, Map<String, Object> data) {
        this.msg = msg;
        this.data = data;
    }

    private Result(ErrorCode errorCode) {
        this.code = errorCode.getCode();
        this.msg = errorCode.getMsg();
    }

    public static Result error(ErrorCode errorCode) {
        return new Result(errorCode);
    }

    public Result addMap(String msg, Object obj) {
        if (this.data == null) {
            this.data = new HashMap<>(5);
        }
        this.data.put(msg, obj);
        return this;
    }

}
