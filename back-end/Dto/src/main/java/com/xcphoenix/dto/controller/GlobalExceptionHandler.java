package com.xcphoenix.dto.controller;

import com.xcphoenix.dto.exception.ServiceLogicException;
import com.xcphoenix.dto.result.ErrorCode;
import com.xcphoenix.dto.result.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


/**
 * @author      xuanc
 * @date        2019/8/9 下午11:41
 * @version     1.0
 */
@ControllerAdvice
@RestController
public class GlobalExceptionHandler {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @ExceptionHandler(Exception.class)
    public Result globalExceptionHandler(Exception ex, HttpServletRequest request) {
        logger.error("发生异常 >>\n",  ex);
        if (ex instanceof ServiceLogicException) {
            return ((ServiceLogicException) ex).errorResult();
        } else if (ex instanceof MethodArgumentNotValidException) {
            // JSR 303 错误
            BindingResult bindingResult = ((MethodArgumentNotValidException) ex).getBindingResult();
            final List<FieldError> fieldErrors = bindingResult.getFieldErrors();
            Result result = Result.error(ErrorCode.BIND_EXCEPTION);
            Map<String, Object> data = new HashMap<>(1);
            List<String> errorList = new LinkedList<>();
            for (FieldError fieldError : fieldErrors) {
                errorList.add(fieldError.getDefaultMessage());
            }
            data.put("errors", errorList);
            result.setData(data);
            return result;
        } else if (ex instanceof BindException) {
            BindException bindException = (BindException) ex;
            List<ObjectError> errors = bindException.getAllErrors();
            ObjectError error = errors.get(0);
            String errorMsg = error.getDefaultMessage();
            return Result.error(ErrorCode.BIND_EXCEPTION.setErrorMsg(errorMsg));
        } else {
            return Result.error(ErrorCode.SERVER_EXCEPTION);
        }
    }

}
