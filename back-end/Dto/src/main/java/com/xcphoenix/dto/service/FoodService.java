package com.xcphoenix.dto.service;

import com.xcphoenix.dto.bean.Food;

import java.io.IOException;

/**
 * @author      xuanc
 * @date        2019/8/15 下午3:10
 * @version     1.0
 */ 
public interface FoodService {

    /**
     * 上架商品
     * @param food 商品信息
     */
    void addFood(Food food) throws IOException;

}
