package com.xcphoenix.dto.service.impl;

import com.alibaba.fastjson.JSON;
import com.xcphoenix.dto.bean.Restaurant;
import com.xcphoenix.dto.exception.ServiceLogicException;
import com.xcphoenix.dto.mapper.RestaurantMapper;
import com.xcphoenix.dto.result.ErrorCode;
import com.xcphoenix.dto.service.Base64ImgService;
import com.xcphoenix.dto.service.RestaurantService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.UUID;

/**
 * @author xuanc
 * @version 1.0
 * @date 2019/8/13 上午9:03
 */
@Service
public class RestaurantServiceImpl implements RestaurantService {

    @Value("${upload.path}")
    private String uploadPath;
    @Value("${upload.url}")
    private String uploadUrl;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private final RestaurantMapper restaurantMapper;
    private final Base64ImgService base64ImgService;

    @Autowired
    public RestaurantServiceImpl(RestaurantMapper restaurantMapper, Base64ImgService base64ImgService) {
        this.restaurantMapper = restaurantMapper;
        this.base64ImgService = base64ImgService;
    }

    @Override
    public boolean isNewShop(Integer userId) {
        return restaurantMapper.hasRestaurant(userId) == null;
    }

    @Override
    public boolean isNameUsable(String name) {
        return restaurantMapper.sameNameRsId(name) == null;
    }

    @Override
    public Restaurant addNewRestaurant(Restaurant restaurant) throws IOException {
        if (!isNewShop(restaurant.getUserId())) {
            throw new ServiceLogicException(ErrorCode.USER_HAVE_SHOP);
        }
        restaurant.setStoreImg(convertPicture(restaurant.getStoreImg(), "store/"));
        restaurant.setLogo(convertPicture(restaurant.getLogo(), "logo/"));
        restaurant.setBannerImg(convertPicture(restaurant.getBannerImg(), "banner/"));

        StringBuilder builder = new StringBuilder();
        for (String instore : restaurant.getInstoreImgs()) {
            String path = convertPicture(instore, "instore/");
            if (builder.length() != 0) {
                builder.append(",");
            }
            builder.append(uploadUrl).append(path);
        }
        restaurant.setInstoreImg(builder.toString());

        restaurantMapper.addRestaurant(restaurant);
        return restaurant;
    }

    private String convertPicture(String base64Str, String directory) throws IOException {
        String path = base64ImgService.base64TransToFile(base64Str, uploadPath + directory + UUID.randomUUID());
        logger.info(directory + ": " + path);
        return uploadUrl + path.substring(path.indexOf(uploadPath) + uploadPath.length());
    }
}
