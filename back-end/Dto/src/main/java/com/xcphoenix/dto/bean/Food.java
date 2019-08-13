package com.xcphoenix.dto.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import java.sql.Timestamp;

/**
 * @author      xuanc
 * @date        2019/8/7 下午8:58
 * @version     1.0
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Food {

    private Integer foodId;
    private Integer restaurantId;

    @Length(max = 20, message = "食品名字超出范围")
    private String name;
    @Length(max = 200, message = "食品描述超出范围")
    private String description;

    private String coverImg;

    private Float originalPrice;
    private Float sellingPrice;
    private Integer totalSale;
    private Integer monthSale;

    private Float favorableRate;

    private Integer salesRestriction;
    private Integer totalNumber;
    private Integer residualAmount;

    private Timestamp gmtCreate;
}
