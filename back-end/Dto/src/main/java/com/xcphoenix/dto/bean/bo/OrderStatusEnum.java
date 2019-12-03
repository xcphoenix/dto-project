package com.xcphoenix.dto.bean.bo;

import lombok.Getter;

import java.util.LinkedList;
import java.util.List;

/**
 * @author      xuanc
 * @date        2019/11/21 下午11:09
 * @version     1.0
 */
@Getter
public enum OrderStatusEnum {

    /**
     * 等待付款
     */
    NEED_PAY(0, false),
    /**
     * 等待商家处理
     */
    WAIT_SHOPPER(1, false),
    /**
     * 配送中
     */
    SENDING(2, false),
    /**
     * 已送达
     */
    SENT(3, true),
    /**
     * 主动取消
     */
    CANCEL(4, true),
    /**
     * 超时取消
     */
    TIMEOUT(5, true);

    private int value;
    private boolean isFinish;

    OrderStatusEnum(int value, boolean isFinish) {
        this.value = value;
        this.isFinish = isFinish;
    }

    public static boolean includeId(int value) {
        for (OrderStatusEnum oe : OrderStatusEnum.values()) {
            if (oe.getValue() == value) {
                return true;
            }
        }
        return false;
    }

    public boolean match(int value) {
        return this.getValue() == value;
    }

    public static List<Integer> currOrderValues() {
        List<Integer> currOrderValues = new LinkedList<>();

        for (OrderStatusEnum orderStatusEnum : OrderStatusEnum.values()) {
            if (!orderStatusEnum.isFinish()) {
                currOrderValues.add(orderStatusEnum.getValue());
            }
        }

        return currOrderValues;
    }

    public static List<Integer> historyOrderValues() {
        List<Integer> historyOrderValues = new LinkedList<>();

        for (OrderStatusEnum orderStatusEnum : OrderStatusEnum.values()) {
            if (orderStatusEnum.isFinish()) {
                historyOrderValues.add(orderStatusEnum.getValue());
            }
        }

        return historyOrderValues;
    }

}
