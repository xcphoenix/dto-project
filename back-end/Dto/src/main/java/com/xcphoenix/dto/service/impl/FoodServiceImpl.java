package com.xcphoenix.dto.service.impl;

import com.xcphoenix.dto.annotation.ShopperCheck;
import com.xcphoenix.dto.bean.Food;
import com.xcphoenix.dto.bean.Foods;
import com.xcphoenix.dto.exception.ServiceLogicException;
import com.xcphoenix.dto.mapper.FoodMapper;
import com.xcphoenix.dto.result.ErrorCode;
import com.xcphoenix.dto.service.Base64ImgService;
import com.xcphoenix.dto.service.FoodCategoryService;
import com.xcphoenix.dto.service.FoodService;
import com.xcphoenix.dto.service.RestaurantService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

/**
 * TODO 删除食品需要处理订单等约束..
 *
 * @author      xuanc
 * @date        2019/8/15 下午3:10
 * @version     1.0
 */
@Service
public class FoodServiceImpl implements FoodService {

    private FoodMapper foodMapper;
    private RestaurantService restaurantService;
    private FoodCategoryService foodCategoryService;
    private Base64ImgService base64ImgService;

    @Value("${upload.image.directory.food}")
    private String foodCoverDire;

    @Value("${food.category.default.name:default}")
    private String defaultCategoryName;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public FoodServiceImpl(FoodMapper foodMapper, RestaurantService restaurantService, FoodCategoryService foodCategoryService, Base64ImgService base64ImgService) {
        this.foodMapper = foodMapper;
        this.restaurantService = restaurantService;
        this.foodCategoryService = foodCategoryService;
        this.base64ImgService = base64ImgService;
    }

    @ShopperCheck
    @Override
    public void addFood(Food food) throws IOException {
        food.setRestaurantId(restaurantService.getLoginShopperResId());
        food.setCoverImg(base64ImgService.convertPicture(food.getCoverImg(), foodCoverDire));
        food.setResidualAmount(food.getTotalNumber());
        foodCategoryService.assertBelongShop(food.getCategoryId());
        try {
            foodMapper.addFood(food);
        } catch (DuplicateKeyException dke) {
            logger.warn("食品信息冲突，触发主键唯一性约束", dke);
            throw new ServiceLogicException(ErrorCode.FOOD_NAME_DUPLICATE);
        }
    }

    @ShopperCheck
    @Override
    public void updateFood(Food food) throws IOException {
        foodCategoryService.assertBelongShop(food.getCategoryId());
        food.setRestaurantId(restaurantService.getLoginShopperResId());
        if (food.getCoverImg() != null) {
            food.setCoverImg(base64ImgService.convertPicture(food.getCoverImg(), foodCoverDire));
        }
        try {
            foodMapper.updateFood(food);
        } catch (DuplicateKeyException dke) {
            logger.warn("食品信息冲突，触发主键唯一性约束", dke);
            throw new ServiceLogicException(ErrorCode.FOOD_NAME_DUPLICATE);
        }
    }

    @ShopperCheck
    @Override
    public Food getFoodDetailById(Long foodId) {
        Food food = foodMapper.getFoodById(foodId, restaurantService.getLoginShopperResId());
        if (food == null) {
            throw new ServiceLogicException(ErrorCode.FOOD_NOT_FOUND);
        }
        if (food.getCategoryId() == null) {
            food.setCategory(defaultCategoryName);
        }
        return food;
    }

    @ShopperCheck
    @Override
    public List<Foods> getAllFoods() {
        Long restaurantId = restaurantService.getLoginShopperResId();
        List<Foods> foodsList = foodMapper.getAllFoods(restaurantId);
        foodsList.add(new Foods(null, defaultCategoryName,
                foodMapper.getFoodsCategoryNull(restaurantId, defaultCategoryName)));
        return foodsList;
    }

    @ShopperCheck
    @Override
    public List<Food> getFoodsByCategory(Long categoryId) {
        Long restaurantId = restaurantService.getLoginShopperResId();
        foodCategoryService.assertBelongShop(categoryId);
        if (categoryId == null) {
            return foodMapper.getFoodsCategoryNull(restaurantId, defaultCategoryName);
        }
        return foodMapper.getFoodsByCategory(categoryId, restaurantId);
    }
}
