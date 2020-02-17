package com.xcphoenix.dto.controller;

import com.xcphoenix.dto.annotation.UserLoginToken;
import com.xcphoenix.dto.result.ErrorCode;
import com.xcphoenix.dto.result.Result;
import com.xcphoenix.dto.service.PayService;
import org.quartz.SchedulerException;
import org.springframework.web.bind.annotation.*;

/**
 * @author      xuanc
 * @date        2019/12/1 下午4:35
 * @version     1.0
 */
@RequestMapping("/pay")
@RestController
public class PayController {

    private PayService payService;

    public PayController(PayService payService) {
        this.payService = payService;
    }

    @UserLoginToken
    @PostMapping("/order/{orderCode}")
    public Result payOrder(@PathVariable Long orderCode,
                           @RequestParam(value = "type", defaultValue = "1") int type) throws SchedulerException {
        boolean isSuccess = payService.payOrder(orderCode, type);
        if (isSuccess) {
            return new Result("支付成功");
        } else {
            return Result.error(ErrorCode.PAY_FAILED);
        }
    }

}
