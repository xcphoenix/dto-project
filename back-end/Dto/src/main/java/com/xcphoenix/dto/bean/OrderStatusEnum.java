package com.xcphoenix.dto.bean;

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
     * 配送中
     */
    SENDING(1),
    /**
     * 已送达
     */
    SENT(2),
    /**
     * 主动取消
     */
    CANCEL(3),
    /**
     * 超时取消
     */
    TIMEOUT(4);

    private int id;

    OrderStatusEnum(int id) {
        this.id = id;
    }

    public static boolean includeId(int id) {
        for (OrderStatusEnum oe : OrderStatusEnum.values()) {
            if (oe.getId() == id) {
                return true;
            }
        }
        return false;
    }

    public int getId() {
        return id;
    }
}
