package com.xcphoenix.dto.service.impl;

import com.xcphoenix.dto.bean.dao.Restaurant;
import com.xcphoenix.dto.exception.ServiceLogicException;
import com.xcphoenix.dto.mapper.CollectionMapper;
import com.xcphoenix.dto.mapper.RestaurantMapper;
import com.xcphoenix.dto.result.ErrorCode;
import com.xcphoenix.dto.service.CollectionService;
import com.xcphoenix.dto.utils.ContextHolderUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author      xuanc
 * @date        2019/10/2 下午4:32
 * @version     1.0
 */
@Service
public class CollectionServiceImpl implements CollectionService {

    private CollectionMapper collectionMapper;
    private RestaurantMapper restaurantMapper;

    public CollectionServiceImpl(CollectionMapper collectionMapper, RestaurantMapper restaurantMapper) {
        this.collectionMapper = collectionMapper;
        this.restaurantMapper = restaurantMapper;
    }

    @Override
    public void collectShop(Long shopId) {
        if (getCollectStatus(shopId)) {
            throw new ServiceLogicException(ErrorCode.SHOP_HAVE_COLLECTED);
        }
        collectionMapper.collectShop(ContextHolderUtils.getLoginUserId(), shopId);
    }

    @Override
    public void cancelCollect(Long shopId) {
        if (!getCollectStatus(shopId)) {
            throw new ServiceLogicException(ErrorCode.SHOP_NOT_COLLECTED);
        }
        collectionMapper.cancelCollection(ContextHolderUtils.getLoginUserId(), shopId);
    }

    @Override
    public Boolean getCollectStatus(Long shopId) {
        if (restaurantMapper.isShopExists(shopId) != 1) {
            throw new ServiceLogicException(ErrorCode.SHOP_NOT_FOUND);
        }
        return collectionMapper.isCollected(ContextHolderUtils.getLoginUserId(), shopId) == 1;
    }

    @Override
    public List<Restaurant> getUserCollection() {
        return collectionMapper.getCollectedShops(ContextHolderUtils.getLoginUserId());
    }

}
