package com.xcphoenix.dto.bean;

import com.xcphoenix.dto.exception.ServiceLogicException;
import com.xcphoenix.dto.result.ErrorCode;
import lombok.Getter;

import java.math.BigDecimal;

/**
 * @author xuanc
 * @version 1.0
 * @date 2019/9/27 下午10:52
 */
@Getter
public class Location {

    private BigDecimal lon;

    private BigDecimal lat;

    private static final String REGEX_LON = "^[\\-+]?(0?\\d{1,2}|0?\\d{1,2}\\.\\d{1,15}|1[0-7]?\\d|1[0-7]?\\d\\.\\d{1,15}|180|180\\.0{1,15})$";
    private static final String REGEX_LAT = "^[\\-+]?([0-8]?\\d|[0-8]?\\d\\.\\d{1,15}|90|90\\.0{1,15})$";

    public Location(double lon, double lat) {
        checkValues(lon, lat);
        this.lon = BigDecimal.valueOf(lon);
        this.lat = BigDecimal.valueOf(lat);
    }

    public static void checkValues(double lon, double lat) {
        if (!java.util.regex.Pattern.matches(REGEX_LON, String.valueOf(lon)) ||
                !java.util.regex.Pattern.matches(REGEX_LAT, String.valueOf(lat))) {
            throw new ServiceLogicException(ErrorCode.illegalArgumentBuilder("无效的经纬度"));
        }
    }

    public void setLon(BigDecimal lon) {
        if (!java.util.regex.Pattern.matches(REGEX_LON, String.valueOf(lon))) {
            throw new ServiceLogicException(ErrorCode.illegalArgumentBuilder("无效的经度"));
        }
        this.lon = lon;
    }

    public void setLat(BigDecimal lat) {
        if (!java.util.regex.Pattern.matches(REGEX_LAT, String.valueOf(lat))) {
            throw new ServiceLogicException(ErrorCode.illegalArgumentBuilder("无效的纬度"));
        }
        this.lat = lat;
    }

}
