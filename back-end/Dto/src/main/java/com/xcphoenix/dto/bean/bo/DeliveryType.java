package com.xcphoenix.dto.bean.bo;

import lombok.Getter;

/**
 * @author xuanc
 * @version 1.0
 * @date 2019/12/1 下午1:29
 */
@Getter
public enum DeliveryType {
    /**
     * 商家自营配送
     */
    SHOP_SELF_SUPPORT("自营配送", 0);

    private String name;
    private int value;

    DeliveryType(String name, int value) {
        this.name = name;
        this.value = value;
    }

    public static boolean isMatched(int value) {
        for (DeliveryType de : DeliveryType.values()) {
            if (de.getValue() == value) {
                return true;
            }
        }
        return false;
    }

}
