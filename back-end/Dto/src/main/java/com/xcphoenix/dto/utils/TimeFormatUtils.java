package com.xcphoenix.dto.utils;

import java.sql.Time;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author      xuanc
 * @date        2019/10/10 下午5:25
 * @version     1.0
 */ 
public class TimeFormatUtils {

    public static Time utcFormat(String timeText) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        LocalDateTime localDateTime = LocalDateTime.parse(timeText, formatter);
        return Time.valueOf(localDateTime.toLocalTime());
    }

}
