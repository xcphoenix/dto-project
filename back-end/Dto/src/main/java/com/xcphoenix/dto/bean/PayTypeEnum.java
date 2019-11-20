package com.xcphoenix.dto.bean;

/**
 * @author xuanc
 * @version 1.0
 * @date 2019/10/26 下午4:40
 */
public enum PayTypeEnum {
    /**
     * 支付宝
     */
    ALIPAY(1);

    int id;

    PayTypeEnum(int id) {
        this.id = id;
    }

    public static boolean includeId(int id) {
        for (PayTypeEnum pe : PayTypeEnum.values()) {
            if (pe.getId() == id) {
                return true;
            }
        }
        return false;
    }

    public static int defaultId() {
        return ALIPAY.getId();
    }

    public int getId() {
        return id;
    }
}
