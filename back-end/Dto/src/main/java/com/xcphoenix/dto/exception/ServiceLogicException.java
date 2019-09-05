package com.xcphoenix.dto.exception;

import com.xcphoenix.dto.result.ErrorCode;
import com.xcphoenix.dto.result.Result;

/**
 * @author      xuanc
 * @date        2019/8/9 下午11:36
 * @version     1.0
 */ 
public class ServiceLogicException extends RuntimeException {

    private ErrorCode errorCode;

    @Override
    public String getMessage() {
        return message;
    }

    private String message;

    public ServiceLogicException(ErrorCode errorCode) {
        this.errorCode = errorCode;
        this.message = errorCode.getMsg();
    }

    public Result errorResult() {
        return Result.error(errorCode);
    }

}
