package com.xcphoenix.dto.exception;

import com.xcphoenix.dto.result.ErrorCode;
import com.xcphoenix.dto.result.Result;

import java.util.Map;

/**
 * @author      xuanc
 * @date        2019/8/9 下午11:36
 * @version     1.0
 */ 
public class ServiceLogicException extends RuntimeException {

    private ErrorCode errorCode;
    private Map<String, Object> extraData;

    public ServiceLogicException(ErrorCode errorCode) {
        super(errorCode.getMsg());
        this.errorCode = errorCode;
    }

    public ServiceLogicException(ErrorCode errorCode, Map<String, Object> extraData) {
        this(errorCode);
        this.extraData = extraData;
    }

    public ServiceLogicException(ErrorCode errorCode, Object extraData) {
        this(errorCode, Map.of("data", extraData));
    }

    public Result errorResult() {
        return Result.error(errorCode);
    }

}
