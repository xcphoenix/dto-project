package com.xcphoenix.dto.bean.dao;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

/**
 * @author xuanc
 * @version 1.0
 * @date 2019/10/9 下午9:55
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Cart {

    Long userId;
    Long restaurantId;
    BigDecimal discountAmount;
    BigDecimal originalTotal;
    BigDecimal total;
    int totalWeight;
    String rstVersion;
    List<CartItem> cartItems;

    private void init() {
        this.discountAmount = new BigDecimal(0);
        this.originalTotal = new BigDecimal(0);
        this.total = new BigDecimal(0);
        this.totalWeight = 0;
    }

    public void compute() {
        init();
        if (cartItems == null) {
            return;
        }
        for (CartItem cartItem : cartItems) {
            this.totalWeight += cartItem.getQuantity();
            this.total.add(cartItem.getSellingPrice());
            this.originalTotal.add(cartItem.getOriginalPrice());
        }
        this.discountAmount = this.originalTotal.subtract(total);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Cart)) {
            return false;
        }
        Cart cart = (Cart) o;
        return getTotalWeight() == cart.getTotalWeight() &&
                Objects.equals(getUserId(), cart.getUserId()) &&
                Objects.equals(getRestaurantId(), cart.getRestaurantId()) &&
                Objects.equals(getDiscountAmount(), cart.getDiscountAmount()) &&
                Objects.equals(getOriginalTotal(), cart.getOriginalTotal()) &&
                Objects.equals(getTotal(), cart.getTotal()) &&
                Objects.equals(getRstVersion(), cart.getRstVersion()) &&
                Objects.equals(getCartItems(), cart.getCartItems());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUserId(), getRestaurantId(), getDiscountAmount(),
                getOriginalTotal(), getTotal(), getTotalWeight(), getCartItems());
    }
}
