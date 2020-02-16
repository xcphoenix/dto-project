package com.xcphoenix.dto.controller;

import com.xcphoenix.dto.annotation.UserLoginToken;
import com.xcphoenix.dto.bean.dao.Cart;
import com.xcphoenix.dto.result.Result;
import com.xcphoenix.dto.service.CartService;
import com.xcphoenix.dto.utils.ContextHolderUtils;
import org.springframework.web.bind.annotation.*;

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
    @GetMapping("/cart/{rstId}")
    public Result getCart(@PathVariable("rstId") Long rstId) {
        Cart cart = cartService.getCart(ContextHolderUtils.getLoginUserId(), rstId);
        return new Result("查询成功").addMap("cart", cart);
    }

    @UserLoginToken
    @PutMapping("/cart")
    public Result updateCart(@RequestBody Cart cart) {
        cart.setUserId(ContextHolderUtils.getLoginUserId());
        cartService.updateCart(cart);
        cart = cartService.getCart(cart.getUserId(), cart.getRestaurantId());
        return new Result("更新成功").addMap("cart", cart);
    }

    @UserLoginToken
    @DeleteMapping("/cart/{rstId}")
    public Result cleanCart(@PathVariable("rstId") Long rstId) {
        cartService.cleanCart(ContextHolderUtils.getLoginUserId(), rstId);
        return new Result("清空购物车成功");
    }

}
