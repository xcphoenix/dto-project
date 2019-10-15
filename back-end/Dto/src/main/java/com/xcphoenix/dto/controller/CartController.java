package com.xcphoenix.dto.controller;

import com.xcphoenix.dto.annotation.UserLoginToken;
import com.xcphoenix.dto.bean.Cart;
import com.xcphoenix.dto.result.Result;
import com.xcphoenix.dto.service.CartService;
import com.xcphoenix.dto.util.ContextHolderUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author      xuanc
 * @date        2019/10/15 下午7:33
 * @version     1.0
 */
@RequestMapping("/shop")
@RestController
public class CartController {

    private CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @UserLoginToken
    @GetMapping("/cart")
    public Result getCart(@RequestParam("restaurantId") Long rstId) {
        Cart cart = cartService.getCart(ContextHolderUtils.getLoginUserId(), rstId);
        return null;
    }

}
