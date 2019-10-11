package com.xcphoenix.dto.service;

import com.xcphoenix.dto.bean.Restaurant;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author xuanc
 * @version 1.0
 * @date 2019/8/12 下午9:10
 */
public interface RestaurantService {

    /**
     * 是否是新商家 - 即：有无开店记录
     *
     * @param userId 用户 id
     * @return 店铺 id
     */
    Integer getUserRestaurantId(Integer userId);

    /**
     * 店铺名是否可用
     *
     * @param name 店铺名
     * @return 可用情况
     */
    boolean isNameUsable(String name);

    /**
     * 添加店铺
     *
     * @param restaurant 店铺信息
     * @return 店铺信息
     * @throws IOException base64url -> picture 图片可能会发生 IO 错误
     */
    Restaurant addNewRestaurant(Restaurant restaurant) throws IOException;

    /**
     * 获取店铺的具体情况（商家端获取信息）
     *
     * @param userId 用户 id
     * @return 店铺详情
     */
    Restaurant getRestaurantDetail(Integer userId);

    /**
     * 获取店铺 id
     *
     * @return 登录商家的店铺id
     */
    public Integer getLoginShopperResId();

    /**
     * 更新店铺信息
     *
     * @param restaurant 店铺信息
     * @throws IOException io
     */
    void updateRestaurant(Restaurant restaurant) throws IOException;

    /**
     * 获取附近的店铺信息，包括距离以及是否在配送范围
     *
     * @param lon 经度
     * @param lat 纬度
     * @param offset 分页偏移量
     * @param limit 分页数量
     * @return map
     * @throws IOException call restful api
     */
    List<Map<String, Object>> getNearbyRestaurants(double lon, double lat,
                                                   Integer offset, Integer limit) throws IOException;

    List<Map<String, Object>> searchRstAsSortType(String text, int type, double lon, double lat,
                                                  Integer from, Integer size) throws IOException;
}
