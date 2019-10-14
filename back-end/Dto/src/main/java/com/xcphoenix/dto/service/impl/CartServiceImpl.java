package com.xcphoenix.dto.service.impl;

import com.xcphoenix.dto.bean.Cart;
import com.xcphoenix.dto.bean.CartItem;
import com.xcphoenix.dto.service.CartService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
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

    public void clearItems(Long userId, Long rstId) {

    }

    public void clearCart(Long userId, Long rstId) {

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
        // 创建 set
        // redisTemplate.opsForSet().add(getItemKey(cartId));

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
        Set<String> diff = redisTemplate.opsForSet()
                .difference(itemKey, foods);
        // remove invalid data
        redisTemplate.opsForSet().remove(itemKey, diff);
        if (diff != null) {
            foods.removeAll(diff);
            // add new data
            redisTemplate.opsForSet().add(itemKey, foods.toArray(String[]::new));
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
            String hashKey = getDetail(cartId, cartItem.getFoodId());
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

    public void updateCart(Cart cart) {
        Long cartId = getCartId(cart.getUserId(), cart.getRestaurantId());
        if (cartId == null) {
            cartId = initCart(cart.getUserId(), cart.getRestaurantId());
        }
        cart.setCartId(cartId);
        modifyCart(cart);
    }

}
