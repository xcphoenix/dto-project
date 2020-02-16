package com.xcphoenix.dto.bean.dao;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @author      xuanc
 * @date        2019/10/22 下午6:14
 * @version     1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderItem {

    private Long orderCode;
    private Long foodId;
    private int quantity;
    private BigDecimal originalPrice;
    private BigDecimal sellingPrice;

    /**
     * 额外字段
     */
    private String exFoodName;
    private String exFoodImgUrl;

    public static OrderItem convert(CartItem cartItem) {
        OrderItem orderItem = new OrderItem();
        orderItem.setFoodId(cartItem.getFoodId());
        orderItem.setQuantity(cartItem.getQuantity());
        orderItem.setSellingPrice(cartItem.getSellingPrice());
        orderItem.setOriginalPrice(cartItem.getOriginalPrice());
        return orderItem;
    }

}
