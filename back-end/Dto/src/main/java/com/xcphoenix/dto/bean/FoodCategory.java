package com.xcphoenix.dto.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

/**
 * @author      xuanc
 * @date        2019/8/7 下午8:44
 * @version     1.0
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class FoodCategory {

    private Integer categoryId;
    private Integer restaurantId;

    @Length(max = 25, message = "分类字数超出范围")
    private String name;
    @Length(max = 25, message = "分类描述超出范围")
    private String description;

}
