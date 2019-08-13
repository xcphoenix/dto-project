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

    public ServiceLogicException(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }

    public Result errorResult() {
        return Result.error(errorCode);
    }

}
