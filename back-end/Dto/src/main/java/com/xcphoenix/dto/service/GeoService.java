package com.xcphoenix.dto.service;

import com.alibaba.fastjson.JSONObject;

import java.math.BigDecimal;

/**
 * @author xuanc
 * @version 1.0
 * @date 2019/9/3 上午9:22
 */
public interface GeoService {

    /**
     * 根据关键字获取搜索建议
     *
     * @param keyword 关键字
     * @return 搜索建议
     */
    JSONObject getSuggestion(String keyword);

    /**
     * 获取地址简要信息
     *
     * @return 地址简要信息
     */
    JSONObject getLocation();

    /**
     * 通过请求方的 ip 获取地理位置
     * @return 调用腾讯地图 api 所返回的数据
     */
    JSONObject getLocAsIp();

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
    String getAreaCode(BigDecimal lat, BigDecimal lng);

    /**
     * 获取城市id
     * @param lat 纬度
     * @param lng 经度
     * @return 城市id
     */
    Integer getAreaId(BigDecimal lat, BigDecimal lng);
}
