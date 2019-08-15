package com.xcphoenix.dto.service.impl;

import com.xcphoenix.dto.annotation.ShopperCheck;
import com.xcphoenix.dto.bean.Restaurant;
import com.xcphoenix.dto.mapper.RestaurantMapper;
import com.xcphoenix.dto.service.Base64ImgService;
import com.xcphoenix.dto.service.RestaurantService;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    public RestaurantServiceImpl(RestaurantMapper restaurantMapper, Base64ImgService base64ImgService) {
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

    @ShopperCheck
    @Override
    public Restaurant addNewRestaurant(Restaurant restaurant) throws IOException {
        restaurant.setStoreImg(base64ImgService.convertPicture(restaurant.getStoreImg(), "store/"));
        restaurant.setLogo(base64ImgService.convertPicture(restaurant.getLogo(), "logo/"));
        restaurant.setBannerImg(base64ImgService.convertPicture(restaurant.getBannerImg(), "banner/"));

        StringBuilder builder = new StringBuilder();
        for (String instore : restaurant.getInstoreImgs()) {
            String path = base64ImgService.convertPicture(instore, "instore/");
            if (builder.length() != 0) {
                builder.append(",");
            }
            builder.append(path);
        }
        restaurant.setInstoreImg(builder.toString());

        restaurantMapper.addRestaurant(restaurant);
        return restaurant;
    }

    @ShopperCheck
    @Override
    public Restaurant getRestaurantDetail(Integer userId) {
        return restaurantMapper.getRestaurantDetail(userId);
    }

}
