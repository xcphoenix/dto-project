package com.xcphoenix.dto.bean.bo;

import lombok.Data;

import java.sql.Time;

/**
 * @author      xuanc
 * @date        2019/10/10 上午9:42
 * @version     1.0
 */
@Data
public class TimeItem {

    private Time begin;
    private Time end;

    public boolean isInItem(Time time) {
        return time.getTime() < this.end.getTime() && time.getTime() >= this.begin.getTime();
    }

}
