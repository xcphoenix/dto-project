package com.xcphoenix.dto.service.impl;

import com.xcphoenix.dto.bean.dao.Cart;
import com.xcphoenix.dto.bean.dao.CartItem;
import com.xcphoenix.dto.bean.dao.Food;
import com.xcphoenix.dto.exception.ServiceLogicException;
import com.xcphoenix.dto.service.CartService;
import com.xcphoenix.dto.service.FoodService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * 获取店铺信息后才会请求购物车信息，在获取店铺商品信息后将数据缓存到 Redis，
 * 对购物车的修改，添加商品、获取商品原价+售价等操作直接从缓存中获取，之后计算其他属性
 * 如果没有得到商品信息，即商品不存在则删除 Redis 中对应的键
 *
 * @author xuanc
 * @version 1.0
 * @date 2019/10/12 下午9:57
 */
@Slf4j
@Service
public class CartServiceImpl implements CartService {

    private RedisTemplate<String, Object> redisTemplate;
    private FoodService foodService;

    public CartServiceImpl(RedisTemplate<String, Object> redisTemplate, FoodService foodService) {
        this.redisTemplate = redisTemplate;
        this.foodService = foodService;
    }

    private String getCartKey(Long userId, Long rstId) {
        return "cart:" + userId + ":" + rstId;
    }

    @Override
    public void cleanCart(Long userId, Long rstId) {
        redisTemplate.delete(getCartKey(userId, rstId));
    }

    @Override
    public void updateCart(Cart cart) {
        String rstVersionKey = "rst:" + cart.getRestaurantId() + ":version";
        Object rstVersionId = redisTemplate.opsForValue().get(rstVersionKey);
        if (rstVersionId == null) {
            redisTemplate.opsForValue().set(rstVersionKey, 1);
            cart.setRstVersion("1");
        } else {
            cart.setRstVersion(String.valueOf(rstVersionId));
        }
        String cartKey = getCartKey(cart.getUserId(), cart.getRestaurantId());
        // filterCartItems(cart).compute();
        // 设置过期时间为三个月
        redisTemplate.opsForValue().set(cartKey, cart, 30 * 3, TimeUnit.DAYS);
    }

    @Override
    public Cart getCart(Long userId, Long rstId) {
        String cartKey = getCartKey(userId, rstId);
        Cart cart = (Cart) redisTemplate.opsForValue().get(cartKey);
        if (cart == null) {
            return null;
        }
        // 检查数据一致性
        String cartRstVersionId = cart.getRstVersion();
        String rstVersionKey = "rst:" + cart.getRestaurantId() + ":version";
        String globalRstVersionId = String.valueOf(redisTemplate.opsForValue().get(rstVersionKey));

        if (cartRstVersionId == null || !cartRstVersionId.equals(globalRstVersionId)) {
            log.info("get cart::data had changed! refresh...");
            filterCartItems(cart).compute();
        }

        return cart;
    }

    private Cart filterCartItems(Cart cart) {
        for(int i = 0; i < cart.getCartItems().size(); ) {
            CartItem cartItem = cart.getCartItems().get(i);
            Long foodId = cartItem.getFoodId();
            try {
                Food food = foodService.getFoodDetailById(foodId);
                food.convertCartItem(cartItem);
            } catch (ServiceLogicException sle) {
                log.info("filter cartItems: food may be deleted");
                cart.getCartItems().remove(i);
                continue;
            }
            i++;
        }
        return cart;
    }

}
