package com.xcphoenix.dto.bean;

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

    public OrderItem(CartItem cartItem) {
        OrderItem orderItem = new OrderItem();
        orderItem.setFoodId(cartItem.getFoodId());
        orderItem.setQuantity(cartItem.getQuantity());
        orderItem.setSellingPrice(BigDecimal.valueOf(cartItem.getSellingPrice()));
        orderItem.setOriginalPrice(BigDecimal.valueOf(cartItem.getOriginalPrice()));
    }

}
