package com.xcphoenix.dto.service.impl;

import ch.hsr.geohash.GeoHash;
import com.xcphoenix.dto.annotation.ShopperCheck;
import com.xcphoenix.dto.bean.Restaurant;
import com.xcphoenix.dto.exception.ServiceLogicException;
import com.xcphoenix.dto.mapper.RestaurantMapper;
import com.xcphoenix.dto.result.ErrorCode;
import com.xcphoenix.dto.service.Base64ImgService;
import com.xcphoenix.dto.service.GeoCoderService;
import com.xcphoenix.dto.service.RestaurantService;
import com.xcphoenix.dto.util.ContextHolderUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * @author xuanc
 * @version 1.0
 * @date 2019/8/13 上午9:03
 */
@Service
public class RestaurantServiceImpl implements RestaurantService {

    private final RestaurantMapper restaurantMapper;
    private final Base64ImgService base64ImgService;

    @Value("${upload.image.directory.store}")
    private String storeImgDire;
    @Value("${upload.image.directory.instore}")
    private String inShoreImgDire;
    @Value("${upload.image.directory.logo}")
    private String logoImgDire;
    @Value("${upload.image.directory.banner}")
    private String bannerImgDire;

    private final int precision = 12;

    @Autowired
    public RestaurantServiceImpl(RestaurantMapper restaurantMapper, Base64ImgService base64ImgService, GeoCoderService geoCoderService) {
        this.restaurantMapper = restaurantMapper;
        this.base64ImgService = base64ImgService;
    }

    @Override
    public Integer getUserRestaurantId(Integer userId) {
        return restaurantMapper.hasRestaurant(userId);
    }

    @Override
    public boolean isNameUsable(String name) {
        return restaurantMapper.sameNameRsId(name) == null;
    }

    @Override
    public Restaurant addNewRestaurant(Restaurant restaurant) throws IOException {
        restaurant.setUserId((Integer)ContextHolderUtils.getRequest().getAttribute("userId"));
        restaurant.setStoreImg(base64ImgService.convertPicture(restaurant.getStoreImg(), storeImgDire));
        restaurant.setLogo(base64ImgService.convertPicture(restaurant.getLogo(), inShoreImgDire));
        restaurant.setBannerImg(base64ImgService.convertPicture(restaurant.getBannerImg(), bannerImgDire));
        restaurant.rangeFormat();

        // pictures
        StringBuilder builder = new StringBuilder();
        for (String instore : restaurant.getInstoreImgs()) {
            String path = base64ImgService.convertPicture(instore, inShoreImgDire);
            if (builder.length() != 0) {
                builder.append(",");
            }
            builder.append(path);
        }
        restaurant.setInstoreImg(builder.toString());

        // geohash
        GeoHash geoHash = GeoHash.withCharacterPrecision(
                restaurant.getAddrLat().doubleValue(),
                restaurant.getAddrLng().doubleValue(), precision
        );
        restaurant.setGeohash(geoHash.toBase32());

        try {
            restaurantMapper.addRestaurant(restaurant);
        } catch (DuplicateKeyException dke) {
            throw new ServiceLogicException(ErrorCode.USER_HAVE_SHOP);
        }
        return restaurant;
    }

    @ShopperCheck
    @Override
    public Restaurant getRestaurantDetail(Integer userId) {
        return restaurantMapper.getRestaurantDetail(userId);
    }

    @Override
    public Integer getLoginShopperResId() {
        Integer userId = (Integer) ContextHolderUtils.getRequest().getAttribute("userId");
        return getUserRestaurantId(userId);
    }

    @Override
    public void updateRestaurant(Restaurant restaurant) throws IOException {
        restaurant.setUserId((Integer) ContextHolderUtils.getRequest().getAttribute("userId"));
        restaurant.setRestaurantId(getLoginShopperResId());
        restaurant.rangeFormat();

        // picture
        if (restaurant.getStoreImg() != null) {
            restaurant.setStoreImg(base64ImgService.convertPicture(restaurant.getStoreImg(), storeImgDire));
        }
        if (restaurant.getLogo() != null) {
            restaurant.setLogo(base64ImgService.convertPicture(restaurant.getLogo(), inShoreImgDire));
        }
        if (restaurant.getBannerImg() != null) {
            restaurant.setBannerImg(base64ImgService.convertPicture(restaurant.getBannerImg(), bannerImgDire));
        }

        if (restaurant.getInstoreImgs() != null) {
            StringBuilder builder = new StringBuilder();
            for (String instore : restaurant.getInstoreImgs()) {
                String path = base64ImgService.convertPicture(instore, inShoreImgDire);
                if (builder.length() != 0) {
                    builder.append(",");
                }
                builder.append(path);
            }
            restaurant.setInstoreImg(builder.toString());
        }

        // geohash
        if (restaurant.getAddrLng() != null && restaurant.getAddrLat() != null) {
            GeoHash geoHash = GeoHash.withCharacterPrecision(
                    restaurant.getAddrLat().doubleValue(),
                    restaurant.getAddrLng().doubleValue(), precision
            );
            restaurant.setGeohash(geoHash.toBase32());
        }

        restaurantMapper.updateRestaurant(restaurant);
    }
}
