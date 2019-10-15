package com.xcphoenix.dto.service.impl;

import com.xcphoenix.dto.bean.Cart;
import com.xcphoenix.dto.bean.CartItem;
import com.xcphoenix.dto.service.CartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

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

    private RedisTemplate<String, String> redisTemplate;

    public CartServiceImpl(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    private String getCartKey(Long userId, Long rstId) {
        return "cart:" + userId + ":" + rstId;
    }

    private String getItemKey(Long cartId) {
        return "cart_item:" + cartId;
    }

    private String getDetail(Long cartId, Long foodId) {
        return "cart_item_detail:" + cartId + ":" + foodId;
    }

    @Override
    public void cleanCart(Long userId, Long rstId) {
        Long cartId = getCartId(userId, rstId);
        String cartKey = getCartKey(userId, rstId);
        String itemKey = getItemKey(cartId);

        if (cartId == null) {
            log.info("cart not found... [userId: " + userId + ", rstId: " + rstId) ;
            return;
        }
        Set<String> foodIds = redisTemplate.opsForSet().members(itemKey);
        if (foodIds != null && foodIds.size() != 0) {
            for (String id : foodIds) {
                String itemDetailKey = getDetail(cartId, Long.valueOf(id));
                Boolean success = redisTemplate.delete(itemDetailKey);
                if (success != null) {
                    log.info("clear Hash [" + itemDetailKey + "] " + (success ? "成功":"失败"));
                }
            }
        }
        redisTemplate.delete(itemKey);
        log.info("clear Hash [" + itemKey + "]... ");
        redisTemplate.delete(cartKey);
        log.info("clear Hash [" + cartKey + "]... ");
    }

    /**
     * 获取购物车 id
     *
     * @param userId 用户 id
     * @param rstId 店铺 id
     * @return 购物车id，若购物车不存在，返回 null
     */
    private Long getCartId(Long userId, Long rstId) {
        return (Long) redisTemplate.opsForHash().get(getCartKey(userId, rstId), "id");
    }

    /**
     * 初始化购物车，获取购物车 id
     *
     * @param userId 用户 id
     * @param rstId  店铺 id
     * @return 购物车 id
     */
    private Long initCart(Long userId, Long rstId) {
        // 获取购物车自增 id
        Long cartId = redisTemplate.opsForValue().increment("cart_id_incr");
        redisTemplate.opsForHash().put(getCartKey(userId, rstId), "id", cartId);

        return cartId;
    }

    /**
     * 更新购物车<br />
     * <b>更新前需判断购物车是否存在，若存在设置 cart.cartId 值</b>
     *
     * @param cart 购物车信息
     */
    private void modifyCart(Cart cart) {
        Long cartId = cart.getCartId();
        String cartKey = getCartKey(cart.getUserId(), cart.getRestaurantId());
        String itemKey = getItemKey(cartId);

        Set<String> foods = new HashSet<>();
        for (CartItem cartItem : cart.getCartItems()) {
            foods.add(String.valueOf(cartItem.getFoodId()));
        }

        Set<String> diff = redisTemplate.opsForSet().members(itemKey);
        if (diff != null) {
            diff.removeAll(foods);
        }

        log.info("insert data => " + foods);
        log.info("diff data => " + diff);

        if (diff != null) {
            // remove invalid data
            redisTemplate.opsForSet().remove(itemKey, diff);
            foods.removeAll(diff);

            // add new data
            if (foods.size() != 0) {
                redisTemplate.opsForSet().add(itemKey, foods.toArray(String[]::new));
            }
        }

        // 获得有效数据
        List<CartItem> cartItemList = cart.getCartItems();
        if (diff != null && diff.size() != 0) {
            for (String foodId: diff) {
                redisTemplate.delete(getDetail(cartId, Long.valueOf(foodId)));
            }
            cartItemList = cart.getCartItems().stream()
                    .filter((CartItem item) -> !diff.contains(String.valueOf(item.getFoodId())))
                    .collect(Collectors.toList());
        }

        cart.init();
        for (CartItem cartItem : cartItemList) {
            cart.compute(cartItem);
            Long foodId = cartItem.getFoodId();

            String hashKey = getDetail(cartId, foodId);
            redisTemplate.opsForHash().put(hashKey, "food_id", foodId);
            redisTemplate.opsForHash().put(hashKey, "quantity", cartItem.getQuantity());
            redisTemplate.opsForHash().put(hashKey, "selling_price", cartItem.getSellingPrice());
            redisTemplate.opsForHash().put(hashKey, "original_price", cartItem.getOriginalPrice());
        }
        cart.computeDiscount();

        // update [cart]
        redisTemplate.opsForHash().put(cartKey, "discount_amount", cart.getDiscountAmount());
        redisTemplate.opsForHash().put(cartKey, "original_total", cart.getOriginalTotal());
        redisTemplate.opsForHash().put(cartKey, "total", cart.getTotal());
        redisTemplate.opsForHash().put(cartKey, "total_weight", cart.getTotalWeight());
    }

    @Override
    public void updateCart(Cart cart) {
        Long cartId = getCartId(cart.getUserId(), cart.getRestaurantId());
        if (cartId == null) {
            cartId = initCart(cart.getUserId(), cart.getRestaurantId());
        }
        cart.setCartId(cartId);
        modifyCart(cart);
    }

    @Override
    public Cart getCart(Long userId, Long rstId) {
        Cart cart = new Cart();
        Long cartId = getCartId(userId, rstId);
        if (cartId == null) {
            return null;
        }
        cart.setCartId(cartId);
        cart.setUserId(userId);
        cart.setRestaurantId(rstId);

        Collection<Object> keys = new ArrayList<>();
        keys.add("discount_amount");
        keys.add("original_total");
        keys.add("total");
        keys.add("total_weight");

        List<Object> values = redisTemplate.opsForHash().multiGet(getCartKey(userId, rstId), keys);
        cart.setDiscountAmount((Float) values.get(0));
        cart.setOriginalTotal((Float) values.get(1));
        cart.setTotal((Float) values.get(2));
        cart.setTotalWeight((Integer) values.get(3));

        keys.clear();
        keys.add("quantity");
        keys.add("selling_price");
        keys.add("original_price");
        keys.add("food_id");
        List<CartItem> cartItemList = new ArrayList<>();
        Set<String> cartItemSet = redisTemplate.opsForSet().members(getItemKey(cartId));

        if (cartItemSet != null) {
            for(String item : cartItemSet) {
                CartItem cartItem = new CartItem();
                values = redisTemplate.opsForHash().multiGet(getDetail(cartId, Long.valueOf(item)), keys);

                cartItem.setQuantity((Integer) values.get(0));
                cartItem.setSellingPrice((Float) values.get(1));
                cartItem.setOriginalPrice((Float) values.get(2));
                cartItem.setFoodId((Long) values.get(3));

                cartItemList.add(cartItem);
            }
        }

        cart.setCartItems(cartItemList);

        return cart;
    }

}
