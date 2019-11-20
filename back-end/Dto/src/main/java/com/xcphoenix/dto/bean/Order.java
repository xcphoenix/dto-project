package com.xcphoenix.dto.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

/**
 * @author      xuanc
 * @date        2019/10/22 上午10:14
 * @version     1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Order {
    /**
     * snow flake
     */
    private Long orderCode;
    private Long rstId;
    private Long userId;
    private Long shipAddrId;
    private int itemCount;
    private BigDecimal discountAmount;
    private BigDecimal packagePrice;
    private BigDecimal deliveryPrice;
    private BigDecimal originalPrice;
    private BigDecimal totalPrice;
    private int payType;
    private Timestamp orderTime;
    private Timestamp payTime;
    private Timestamp invalidTime;
    private int deliveryType;
    private String remark;
    private int status;

    /**
     * 冗余字段
     */
    private String exShipAddr;
    private String exUserName;
    private String exUserPhone;
    private String exRstName;
    private String exRstLogoUrl;

    private List<OrderItem> orderItems;

}
