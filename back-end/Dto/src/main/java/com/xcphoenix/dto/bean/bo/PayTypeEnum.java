package com.xcphoenix.dto.bean.bo;

import lombok.Getter;

/**
 * @author xuanc
 * @version 1.0
 * @date 2019/10/26 下午4:40
 */
@Getter
public enum PayTypeEnum {
    /**
     * 支付宝
     */
    ALIPAY("支付宝", 1);

    private String name;
    private int value;

    PayTypeEnum(String name, int value) {
        this.name = name;
        this.value = value;
    }

    public static boolean isMatched(int value) {
        for (PayTypeEnum pe : PayTypeEnum.values()) {
            if (pe.getValue() == value) {
                return false;
            }
        }
        return true;
    }

    public static int defaultValue() {
        return ALIPAY.getValue();
    }

}
