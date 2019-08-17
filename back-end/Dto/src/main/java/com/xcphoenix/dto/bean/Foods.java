package com.xcphoenix.dto.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author      xuanc
 * @date        2019/8/16 下午11:02
 * @version     1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Foods {

    private Integer categoryId;
    private String categoryName;
    List<Food> foodsDetail;

}

