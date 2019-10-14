package com.xcphoenix.dto.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author      xuanc
 * @date        2019/10/9 下午9:55
 * @version     1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartItem {

    Long cartId;
    Long foodId;
    int quantity;
    float originalPrice;
    float sellingPrice;

}
