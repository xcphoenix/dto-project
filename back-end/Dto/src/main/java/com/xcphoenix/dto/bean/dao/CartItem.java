package com.xcphoenix.dto.bean.dao;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * @author xuanc
 * @version 1.0
 * @date 2019/10/9 下午9:55
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartItem {

    Long foodId;
    int quantity;
    BigDecimal originalPrice;
    BigDecimal sellingPrice;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CartItem)) {
            return false;
        }
        CartItem cartItem = (CartItem) o;
        return getQuantity() == cartItem.getQuantity() &&
                getFoodId().equals(cartItem.getFoodId()) &&
                getOriginalPrice().equals(cartItem.getOriginalPrice()) &&
                getSellingPrice().equals(cartItem.getSellingPrice());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getFoodId(), getQuantity(), getOriginalPrice(), getSellingPrice());
    }
}
