package com.xcphoenix.dto.service;

import com.xcphoenix.dto.bean.Restaurant;

import java.util.List;

/**
 * @author xuanc
 * @version 1.0
 * @date 2019/10/2 下午4:32
 */
public interface CollectionService {

    /**
     * 收藏店铺
     * @param shopId 店铺id
     */
    void collectShop(Integer shopId);

    /**
     * 取消收藏
     * @param shopId 店铺id
     */
    void cancelCollect(Integer shopId);

    /**
     * 获取店铺收藏状态<br />
     * <em>need login</em>
     * @param shopId 店铺id
     * @return 状态
     */
    Boolean getCollectStatus(Integer shopId);

    /**
     * 获取用户店铺收藏状态
     *
     * @return 收藏的店铺列表ai状态
     */
    List<Restaurant> getUserCollection();

}
