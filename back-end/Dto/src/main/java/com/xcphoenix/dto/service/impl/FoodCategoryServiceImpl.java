package com.xcphoenix.dto.service.impl;

import com.xcphoenix.dto.annotation.ShopperCheck;
import com.xcphoenix.dto.bean.FoodCategory;
import com.xcphoenix.dto.exception.ServiceLogicException;
import com.xcphoenix.dto.mapper.FoodCategoryMapper;
import com.xcphoenix.dto.result.ErrorCode;
import com.xcphoenix.dto.service.FoodCategoryService;
import com.xcphoenix.dto.service.RestaurantService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author xuanc
 * @version 1.0
 * @date 2019/8/15 上午8:26
 */
@Service
@ShopperCheck
public class FoodCategoryServiceImpl implements FoodCategoryService {

    private FoodCategoryMapper foodCategoryMapper;
    private RestaurantService restaurantService;
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Value("${food.category.default.name:default}")
    private String defaultCategoryName;

    @Autowired
    public FoodCategoryServiceImpl(FoodCategoryMapper foodCategoryMapper, RestaurantService restaurantService) {
        this.foodCategoryMapper = foodCategoryMapper;
        this.restaurantService = restaurantService;
    }

    @Override
    public void addNewCategory(FoodCategory foodCategory) {
        if (defaultCategoryName.equals(foodCategory.getName())) {
            throw new ServiceLogicException(ErrorCode.CATEGORY_NAME_CONFLICT);
        }
        foodCategory.setRestaurantId(restaurantService.getLoginShopperResId());
        try {
            foodCategoryMapper.addNewCategory(foodCategory);
        } catch (DuplicateKeyException dke) {
            logger.warn("食品分类冲突，触发唯一性约束条件：", dke);
            throw new ServiceLogicException(ErrorCode.CATEGORY_DUPLICATE);
        }
    }

    @Override
    public void updateCategory(FoodCategory foodCategory) {
        Long restaurantId = restaurantService.getLoginShopperResId();
        foodCategory.setRestaurantId(restaurantId);
        if (foodCategory.getCategoryId() == null || foodCategoryMapper.checkHaveCategories(foodCategory.getCategoryId(), restaurantId) != 1) {
            throw new ServiceLogicException(ErrorCode.CATEGORY_NOT_FOUND);
        }
        if (foodCategory.getName() != null && defaultCategoryName.equals(foodCategory.getName())) {
            throw new ServiceLogicException(ErrorCode.CATEGORY_NAME_CONFLICT);
        }
        try {
            foodCategoryMapper.updateCategory(foodCategory);
        } catch (DuplicateKeyException dke) {
            logger.warn("食品分类冲突，触发唯一性约束条件：", dke);
            throw new ServiceLogicException(ErrorCode.CATEGORY_DUPLICATE);
        }
    }

    @Override
    public void deleteCategory(Long categoryId) {
        if (foodCategoryMapper.deleteCategory(categoryId, restaurantService.getLoginShopperResId()) == 0) {
            throw new ServiceLogicException(ErrorCode.CATEGORY_NOT_FOUND);
        }
    }

    @Override
    public List<FoodCategory> getCategories() {
        return foodCategoryMapper.getCategories(restaurantService.getLoginShopperResId());
    }

    @Override
    public void assertBelongShop(Long categoryId) {
        if (categoryId != null &&
                foodCategoryMapper.checkHaveCategories(categoryId, restaurantService.getLoginShopperResId()) != 1) {
            throw new ServiceLogicException(ErrorCode.CATEGORY_NOT_FOUND);
        }
    }

}
