package com.xcphoenix.dto.controller;

import com.xcphoenix.dto.annotation.UserLoginToken;
import com.xcphoenix.dto.bean.ao.OrderModify;
import com.xcphoenix.dto.bean.bo.OrderStatusEnum;
import com.xcphoenix.dto.bean.dao.Order;
import com.xcphoenix.dto.bean.dto.PageObject;
import com.xcphoenix.dto.result.ErrorCode;
import com.xcphoenix.dto.result.Result;
import com.xcphoenix.dto.service.OrderService;
import com.xcphoenix.dto.utils.ContextHolderUtils;
import org.quartz.SchedulerException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.Map;

/**
 * TODO Websocket连接, 实时刷新订单状态
 *
 * @author      xuanc
 * @date        2019/10/21 下午10:23
 * @version     1.0
 */
@RestController
@RequestMapping("/order")
public class OrderController {

    private OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @UserLoginToken
    @GetMapping("/pre/{rstId}")
    public Result getPreOrder(@PathVariable("rstId") Long rstId) {
        Map<String, Object> preData = orderService.getPreviewData(rstId);
        return new Result().addMap("preOrder", preData);
    }

    @UserLoginToken
    @PostMapping("/{rstId}")
    public Result purchaseOrder(@PathVariable("rstId") Long rstId, @RequestBody Order order)
            throws SchedulerException {
        order.setRstId(rstId);
        Order newOrder = orderService.purchaseNewOrder(order);
        newOrder.convertId();
        return new Result().addMap("newOrder", newOrder);
    }

    @UserLoginToken
    @PutMapping("/{orderCode}")
    public Result modifyOrder(@PathVariable("orderCode") Long orderCode,
                              @Validated @RequestBody OrderModify orderModify) {
        orderService.modifyOrder(orderCode, orderModify);
        return new Result();
    }

    @UserLoginToken
    @PutMapping("/cancel/{orderCode}")
    public Result cancelOrder(@PathVariable("orderCode") Long orderCode) {
        orderService.cancelOrder(orderCode);
        return new Result();
    }

    @UserLoginToken
    @DeleteMapping("/{orderCode}")
    public Result delOrder(@PathVariable("orderCode") Long orderCode) {
        orderService.delOrder(orderCode);
        return new Result();
    }

    @UserLoginToken
    @GetMapping("/current")
    public Result getCurrentOrders(@RequestParam("from") int from, @RequestParam("size") int size) {
        PageObject<Order> orderList = orderService.getCurrentOrders(from, size);
        orderList.getData().forEach(Order::convertId);
        return new Result().addMap("currOrders", orderList);
    }

    @UserLoginToken
    @GetMapping("/history")
    public Result getHistoryOrders(@RequestParam("from") int from, @RequestParam("size") int size) {
        PageObject<Order> orderList = orderService.getHistoryOrders(from, size);
        orderList.getData().forEach(Order::convertId);
        return new Result().addMap("historyOrders", orderList);
    }

    @UserLoginToken
    @GetMapping("/all")
    public Result getOrders(@RequestParam("from") int from, @RequestParam("size") int size) {
        PageObject<Order> orderList = orderService.getOrders(from, size);
        orderList.getData().forEach(Order::convertId);
        return new Result().addMap("orders", orderList);
    }

    @UserLoginToken
    @GetMapping("/all/nopage")
    public Result getOrders() {
        PageObject<Order> orderList = orderService.getOrders();
        orderList.getData().forEach(Order::convertId);
        return new Result().addMap("orders", orderList);
    }

    @UserLoginToken
    @GetMapping("/detail/{orderCode}")
    public Result getOrderDetail(@PathVariable("orderCode") Long orderCode) {
        Order orderDetail = orderService.getOrderById(orderCode, ContextHolderUtils.getLoginUserId());
        if (orderDetail != null) {
            orderDetail.convertId();
            return new Result().addMap("detail", orderDetail);
        } else {
            return Result.error(ErrorCode.ORDER_INVALID);
        }

    }

    /**
     * 获取订单状态
     *
     * @param orderCode 订单编号
     * @return 订单状态，如果订单为未支付，返回倒计时
     */
    @UserLoginToken
    @GetMapping("/status/{orderCode}")
    public Result getOrderStatus(@PathVariable("orderCode") Long orderCode) {
        Integer status = orderService.getOrderStatus(orderCode);
        if (status == null) {
            return Result.error(ErrorCode.ORDER_INVALID);
        }
        if (OrderStatusEnum.NEED_PAY.match(status)) {
            Timestamp timestamp = orderService.getInvalidTime(orderCode);
            Long jetLag = timestamp.getTime() - System.currentTimeMillis();
            return new Result()
                    .addMap("isFinished", false)
                    .addMap("untilInvalidTime", jetLag);
        } else {
            OrderStatusEnum orderStatusEnum = OrderStatusEnum.getStatusEnum(status);
            return new Result()
                    .addMap("isFinished", true)
                    .addMap("status", orderStatusEnum);
        }
    }

}
