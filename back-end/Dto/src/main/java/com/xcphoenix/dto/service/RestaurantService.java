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
     * 店铺是否存在
     *
     * @param rstId 店铺id
     * @return boolean
     */
    boolean isExists(Long rstId);

    /**
     * 是否是新商家 - 即：有无开店记录
     *
     * @param userId 用户 id
     * @return 店铺 id
     */
    Long getUserRestaurantId(Long userId);

    /**
     * 店铺名是否可用
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
     * @return 店铺详情
     */
    Restaurant getRstByShopper();

    /**
     * 获取店铺信息
     */
    Restaurant getRstDetail(Long rstId);

    /**
     * 获取店铺 id
     *
     * @return 登录商家的店铺id
     */
    public Long getLoginShopperResId();

    /**
     * 更新店铺信息
     * @return
     */
    Restaurant updateRestaurant(Restaurant restaurant) throws IOException;

    /**
     * 获取店铺信息摘要
     */
    List<Map<String, Object>> getRstRemark(int type, double lon, double lat, Integer from, Integer size) throws IOException;

    /**
     * 根据关键字获取店铺摘要
     */
    List<Map<String, Object>> getRstRemarkWithSearch(String text, int type, double lon, double lat, Integer from, Integer size)
            throws IOException;

}
