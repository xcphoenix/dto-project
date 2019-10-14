package com.xcphoenix.dto;

import com.alibaba.fastjson.JSON;
import com.xcphoenix.dto.bean.Cart;
import com.xcphoenix.dto.bean.CartItem;
import com.xcphoenix.dto.service.impl.CartServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;

/**
 * @author      xuanc
 * @date        2019/10/14 下午10:35
 * @version     1.0
 */
@SpringBootTest
@ExtendWith(SpringExtension.class)
@Slf4j
class CartTest {

    @Autowired
    private CartServiceImpl cartService;

    @Test
    void testAddCart() {
        Cart cart = new Cart();
        cart.setUserId(999999L);
        cart.setRestaurantId(888888L);
        List<CartItem> cartItemList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
             CartItem cartItem = new CartItem();
             cartItem.setFoodId((long) i);
             cartItem.setQuantity((int)(Math.random() * 6) + 1);
             cartItem.setOriginalPrice((long)(Math.random() * 100) % 50);
             cartItem.setSellingPrice((float) (cartItem.getOriginalPrice() - Math.random() * 10));

             cartItemList.add(cartItem);
        }
        cart.setCartItems(cartItemList);

        log.info("cart data ==> " + JSON.toJSONString(cart));

        cartService.updateCart(cart);
    }

}
