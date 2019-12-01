package com.xcphoenix.dto.bean.bo;

/**
 * @author xuanc
 * @version 1.0
 * @date 2019/10/3 下午4:43
 */
public enum SortType {
    /**
     * 综合评分
     */
    DEFAULT,
    /**
     * 月销售量
     */
    SALE,
    /**
     * 距离
     */
    DISTANCE,
    /**
     * 好评优先(评分)
     */
    SCORE,
    /**
     * 起送价最低
     */
    MIN_PRICE,
    /**
     * 运费最低
     */
    DELIVERY_PRICE,
    /**
     * 配送最快
     */
    DELIVERY_TIME,
    /**
     * 人均消费高到低
     */
    PER_consumption_HIGH,
    /**
     * 人均消费低到高
     */
    PER_consumption_LOW;
}
