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
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

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
    private RedisTemplate<String, Object> redisTemplate;

    @Value("${upload.image.directory.food}")
    private String foodCoverDire;

    @Value("${food.category.default.name:default}")
    private String defaultCategoryName;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public FoodServiceImpl(FoodMapper foodMapper, RestaurantService restaurantService,
                           FoodCategoryService foodCategoryService, Base64ImgService base64ImgService,
                           RedisTemplate<String, Object> redisTemplate) {
        this.foodMapper = foodMapper;
        this.restaurantService = restaurantService;
        this.foodCategoryService = foodCategoryService;
        this.base64ImgService = base64ImgService;
        this.redisTemplate = redisTemplate;
    }

    @Override
    public boolean isExists(Long foodId, Long rstId) {
        return foodMapper.exists(foodId, rstId) == 1;
    }

    @ShopperCheck
    @Override
    @CachePut(value = "foodCacheManager", key = "'food:' + #result.foodId + ':detail'")
    public Food addFood(Food food) throws IOException {
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
        refreshRstVerId(food.getRestaurantId());
        return food;
    }

    @ShopperCheck
    @Override
    @Caching(
            put = {
                    @CachePut(value = "foodCacheManager", key = "'food:' + #food.foodId + ':detail'")
            },
            evict = {
                    @CacheEvict(value = "foodCacheManager", key = "'rst:' + #restaurantServiceImpl.getLoginShopperResId() + ':foods'")
            }
    )
    public Food updateFood(Food food) throws IOException {
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
        refreshRstVerId(restaurantService.getLoginShopperResId());
        return getFoodDetailById(food.getFoodId());
    }

    @Override
    @Cacheable(value = "foodCacheManager", key = "'food:' + #foodId + ':detail'")
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
    public List<Foods> getAllFoodsByShopper() {
        Long restaurantId = restaurantService.getLoginShopperResId();
        return foodMapper.getAllFoods(restaurantId);
    }

    @Override
    @Cacheable(value = "foodCacheManager", key = "'rst:' + #rstId + ':foods'")
    public List<Foods> getAllFoodsByRstId(Long rstId) {
        if (!restaurantService.isExists(rstId)) {
            throw new ServiceLogicException(ErrorCode.SHOP_NOT_FOUND);
        }
        return foodMapper.getAllFoods(rstId);
    }

    @Override
    public List<Food> getFoodsByCategory(Long categoryId) {
        Long restaurantId = restaurantService.getLoginShopperResId();
        foodCategoryService.assertBelongShop(categoryId);
        return foodMapper.getFoodsByCategory(categoryId, restaurantId);
    }

    @ShopperCheck
    @Override
    @Caching(
            evict = {
                    @CacheEvict(value = "foodCacheManager", key = "'food:' + #foodId + ':detail'"),
                    @CacheEvict(value = "foodCacheManager", key = "'rst:' + #restaurantServiceImpl.getLoginShopperResId() + ':foods'")
            }
    )
    public void delFoodById(Long foodId) {
        Long rstId = restaurantService.getLoginShopperResId();
        if (!isExists(foodId, rstId)) {
            throw new ServiceLogicException(ErrorCode.FOOD_NOT_FOUND);
        }
        foodMapper.delFood(foodId, rstId);
        refreshRstVerId(rstId);
    }

    @ShopperCheck
    @Override
    @Caching(
            put = {
                    @CachePut(value = "foodCacheManager", key = "'food:' + #foodId + ':detail'")
            },
            evict = {
                    @CacheEvict(value = "foodCacheManager", key = "'rst:' + #restaurantServiceImpl.getLoginShopperResId() + ':foods'")
            }
    )
    public Food changeCategory(Long foodId, Long newCategoryId) {
        foodCategoryService.assertBelongShop(newCategoryId);
        foodMapper.changeCategory(foodId, newCategoryId);
        // 刷新缓存
        refreshRstVerId(restaurantService.getLoginShopperResId());
        // 自调用不会使用缓存
        return getFoodDetailById(foodId);
    }

    @Override
    public Set<Long> filterFoodsOfLackStock(Long rstId, Map<Long, Integer> baseData) {
        Map<Long, Integer> idWithStock = foodMapper.getFoodsStock(rstId)
                .stream().collect(Collectors.toMap(Food::getFoodId, Food::getResidualAmount));
        Set<Long> idList = baseData.keySet();
        baseData.forEach((key, value) -> {
            if (idWithStock.containsKey(key) || idWithStock.get(key) >= value) {
                idList.remove(key);
            }
        });
        return idList;
    }

    private void refreshRstVerId(Long rstId) {
        String rstVersionKey = "rst:" + rstId + ":version";
        redisTemplate.opsForValue().increment(rstVersionKey);
    }

}