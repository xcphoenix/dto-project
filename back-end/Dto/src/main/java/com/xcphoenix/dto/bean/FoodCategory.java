package com.xcphoenix.dto.bean;

import com.xcphoenix.dto.validator.ValidateGroup;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

/**
 * @author      xuanc
 * @date        2019/8/7 下午8:44
 * @version     1.0
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class FoodCategory {

    private Long categoryId;
    private Long restaurantId;

    @Length(max = 25, message = "分类字数超出范围")
    @NotBlank(message = "分类名不能为空", groups = {ValidateGroup.AddData.class})
    private String name;

    @NotBlank(message = "分类描述不能为空", groups = {ValidateGroup.AddData.class})
    @Length(max = 25, message = "分类描述超出范围")
    private String description;

}
