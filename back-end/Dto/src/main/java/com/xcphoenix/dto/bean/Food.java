package com.xcphoenix.dto.bean;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
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

    private Long foodId;
    private Long restaurantId;
    private Long categoryId;

    @JSONField(deserialize = false)
    private String category;

    @Length(max = 20, message = "食品名字超出范围")
    private String name;
    @Length(max = 200, message = "食品描述超出范围")
    private String description;

    private String coverImg;

    @DecimalMin(value = "0.0", message = "价格不能为负")
    private Float originalPrice;
    @DecimalMin(value = "0.0", message = "价格不能为负")
    private Float sellingPrice;
    private Integer totalSale;
    private Integer monthSale;

    private Float favorableRate;

    @Min(value = 0, message = "限购数量不能为负数")
    private Integer salesRestriction;
    @Min(value = 0, message = "总量不能为负数")
    private Integer totalNumber;
    private Integer residualAmount;

    private Timestamp gmtCreate;
}