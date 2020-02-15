package com.xcphoenix.dto.bean.bo;

import lombok.Getter;

/**
 * @author xuanc
 * @version 1.0
 * @date 2019/12/4 下午4:04
 */
@Getter
public enum RateType {
    /**
     * 吐槽、较差、一般、满意、超赞等评价
     */
    ROAST("吐槽", 1, RateStatus.BAD),
    TERRIBLE("较差", 2, RateStatus.BAD),
    GENERALLY("一般", 3, RateStatus.NORMAL),
    SATISFIED("满意", 4, RateStatus.GOOD),
    EXCELLENT("超赞",  5, RateStatus.GOOD);

    private String name;
    private int value;
    private int status;

    RateType(String name, int value, RateStatus status) {
        this.name = name;
        this.value = value;
        this.status = status.value;
    }

    enum RateStatus{
        /**
         * 评价概括
         */
        GOOD(1),
        BAD(-1),
        NORMAL(0);

        int value;

        RateStatus(int value) {
            this.value = value;
        }

    }

}
