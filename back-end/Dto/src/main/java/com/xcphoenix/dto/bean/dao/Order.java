package com.xcphoenix.dto.bean.dao;

import com.alibaba.fastjson.annotation.JSONField;
import com.xcphoenix.dto.bean.bo.OrderStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

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
    /**
     * 处理前端丢失精度...
     */
    @JSONField(deserialize = false)
    private String orderId;
    private Long rstId;
    private Long userId;
    private Long shipAddrId;
    private int itemCount;
    private BigDecimal discountAmount;
    private BigDecimal packagePrice;
    private BigDecimal deliveryPrice;
    private BigDecimal originalPrice;
    private BigDecimal totalPrice;
    private BigDecimal price;
    private int payType;
    private Timestamp orderTime;
    private Timestamp payTime;
    private Timestamp invalidTime;
    private int deliveryType;
    @Length(max = 100, message = "备注长度超出限制")
    private String remark;
    private int status;
    /**
     * 订单状态描述
     */
    @JSONField(deserialize = false)
    private String statusDesc;

    /**
     * 冗余字段
     */
    private String exShipAddr;
    private String exUserName;
    private String exUserPhone;
    private String exRstName;
    private String exRstLogoUrl;

    private List<OrderItem> orderItems;

    public void convertId() {
        if (orderCode != null) {
            orderId = String.valueOf(orderCode);
        }
        statusDesc = OrderStatusEnum.getStatusEnum(status).getName();
    }

}
