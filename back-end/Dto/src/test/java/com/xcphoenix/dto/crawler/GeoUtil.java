package com.xcphoenix.dto.crawler;

import java.math.BigDecimal;
import java.util.Random;

/**
 * @author      xuanc
 * @date        2019/10/3 下午8:58
 * @version     1.0
 */ 
public class GeoUtil {

    public static String[] randomLonLat(double MinLon, double MaxLon, double MinLat, double MaxLat) {
        Random random = new Random();
        BigDecimal db = new BigDecimal(Math.random() * (MaxLon - MinLon) + MinLon);
        String lon = db.setScale(6, BigDecimal.ROUND_HALF_UP).toString();// 小数后6位
        db = new BigDecimal(Math.random() * (MaxLat - MinLat) + MinLat);
        String lat = db.setScale(6, BigDecimal.ROUND_HALF_UP).toString();
        return new String[]{lon, lat};
    }
}
