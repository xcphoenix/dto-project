package com.xcphoenix.dto.bean.bo;

/**
 * @author      xuanc
 * @date        2019/11/21 下午11:09
 * @version     1.0
 */ 
public enum OrderStatusEnum {

    /**
     * 等待付款
     */
    NEED_PAY(0),
    /**
     * 等待商家处理
     */
    WAIT_SHOPPER(1),
    /**
     * 配送中
     */
    SENDING(2),
    /**
     * 已送达
     */
    SENT(3),
    /**
     * 主动取消
     */
    CANCEL(4),
    /**
     * 超时取消
     */
    TIMEOUT(5);

    private int value;

    OrderStatusEnum(int value) {
        this.value = value;
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

    public int getValue() {
        return value;
    }
}
