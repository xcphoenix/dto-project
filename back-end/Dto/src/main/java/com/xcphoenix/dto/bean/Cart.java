package com.xcphoenix.dto.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author      xuanc
 * @date        2019/10/9 下午9:55
 * @version     1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Cart {

    Long cartId;
    Long userId;
    Long restaurantId;
    float discountAmount;
    float originalTotal;
    float total;
    int totalWeight;
    List<CartItem> cartItems;

    /**
     * 初始化数据
     */
    public void init() {
        this.discountAmount = 0;
        this.originalTotal = 0;
        this.total = 0;
        this.totalWeight = 0;
    }

    public void computeDiscount() {
        this.discountAmount = this.originalTotal - this.total;
    }

    public void compute(CartItem cartItem) {
        this.totalWeight += cartItem.getQuantity();
        this.total += cartItem.getSellingPrice();
        this.originalTotal += cartItem.getOriginalPrice();
    }

}
