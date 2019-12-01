package com.xcphoenix.dto.bean.dao;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    float originalPrice;
    float sellingPrice;

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
                Float.compare(cartItem.getOriginalPrice(), getOriginalPrice()) == 0 &&
                Float.compare(cartItem.getSellingPrice(), getSellingPrice()) == 0 &&
                Objects.equals(getFoodId(), cartItem.getFoodId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getFoodId(), getQuantity(), getOriginalPrice(), getSellingPrice());
    }
}
