package com.xcphoenix.dto;

import com.xcphoenix.dto.service.CartService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 * @author      xuanc
 * @date        2019/10/14 下午10:35
 * @version     1.0
 */
@SpringBootTest
@ExtendWith(SpringExtension.class)
@Slf4j
@Disabled
class CartTest {

    @Autowired
    private CartService cartService;

    @Test
    void testAddCart() {
        // Cart cart = new Cart();
        // cart.setUserId(999999L);
        // cart.setRestaurantId(888888L);
        // List<CartItem> cartItemList = new ArrayList<>();
        // for (int i = 0; i < 5; i++) {
        //      CartItem cartItem = new CartItem();
        //      cartItem.setFoodId((long) i);
        //      cartItem.setQuantity((int)(Math.random() * 6) + 1);
        //      cartItem.setOriginalPrice((long)(Math.random() * 100) % 50);
        //      cartItem.setSellingPrice(Math.abs((float) (cartItem.getOriginalPrice() - Math.random() * 10)));
        //      log.info("[make data] random cartItem ==> " + JSON.toJSON(cartItem));
        //      cartItemList.add(cartItem);
        // }
        // cart.setCartItems(cartItemList);
        // cartService.updateCart(cart);
        // String saveJson = JSON.toJSONString(cart);
        // log.info("[Insert] cart data ==> " + JSON.toJSONString(cart));
        //
        // cart = cartService.getCart(cart.getUserId(), cart.getRestaurantId());
        // log.info("[Get] cart data ==> " + JSON.toJSONString(cart));
        // assertTrue(JSON.parseObject(saveJson, Cart.class).equals(cart));
        //
        // // cartService.cleanCart(999999L, 888888L);
        // //
        // // cart = cartService.getCart(cart.getUserId(), cart.getRestaurantId());
        // // log.info("[After clean] cart data ==> " + JSON.toJSONString(cart));
        // // assertNull(cart);

    }

}
