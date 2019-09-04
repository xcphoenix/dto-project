package com.xcphoenix.dto.service;

import com.alibaba.fastjson.JSONObject;

import java.math.BigDecimal;

/**
 * @author xuanc
 * @version 1.0
 * @date 2019/9/3 上午9:22
 */
public interface GeoCoderService {

    /**
     * 腾讯地图 webservice - 逆地址解析
     * @param lat 纬度
     * @param lng 经度
     * @return 调用 api 返回的 json 数据
     */
    JSONObject getLocMsg(BigDecimal lat, BigDecimal lng);

    /**
     * 根据经纬度获取城市代码
     * @param lat 纬度
     * @param lng 经度
     * @return 城市代码
     */
    String getCityCode(BigDecimal lat, BigDecimal lng);
}
