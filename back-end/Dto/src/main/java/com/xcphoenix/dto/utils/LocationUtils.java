package com.xcphoenix.dto.utils;

import com.xcphoenix.dto.exception.ServiceLogicException;
import com.xcphoenix.dto.result.ErrorCode;

import java.util.regex.Pattern;

/**
 * @author xuanc
 * @version 1.0
 * @date 2019/9/27 下午10:52
 */
public class LocationUtils {

    private static final Pattern REGEX_LON = Pattern.compile(
            "^[\\-+]?(0?\\d{1,2}|0?\\d{1,2}\\.\\d{1,15}|1[0-7]?\\d|1[0-7]?\\d\\.\\d{1,15}|180|180\\.0{1,15})$");
    private static final Pattern REGEX_LAT = Pattern.compile(
            "^[\\-+]?([0-8]?\\d|[0-8]?\\d\\.\\d{1,15}|90|90\\.0{1,15})$");

    public static void assertLegalValues(double lon, double lat) {
        if (!REGEX_LON.matcher(String.valueOf(lon)).matches() ||
                !REGEX_LAT.matcher(String.valueOf(lat)).matches()) {
            throw new ServiceLogicException(ErrorCode.illegalArgumentBuilder("无效的经纬度"));
        }
    }

}
