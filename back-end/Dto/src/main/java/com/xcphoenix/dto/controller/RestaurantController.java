package com.xcphoenix.dto.controller;

import com.xcphoenix.dto.annotation.UserLoginToken;
import com.xcphoenix.dto.bean.dao.Restaurant;
import com.xcphoenix.dto.result.Result;
import com.xcphoenix.dto.service.RestaurantService;
import com.xcphoenix.dto.validator.ValidateGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * TODO 打开店铺页面，查询店铺信息、购物车信息、商品信息、是否收藏店铺
 * @author xuanc
 * @version 1.0
 * @date 2019/8/12 下午9:09
 */
@RestController
@RequestMapping("/shop")
public class RestaurantController {

    private RestaurantService restaurantService;

    @Autowired
    public RestaurantController(RestaurantService restaurantService) {
        this.restaurantService = restaurantService;
    }

    /**
     * 商家 - 添加店铺
     */
    @UserLoginToken
    @PostMapping("/restaurant")
    public Result addNewRestaurant(@Validated(ValidateGroup.AddData.class) @RequestBody Restaurant restaurant) throws IOException {
        Restaurant newRst = restaurantService.addNewRestaurant(restaurant);
        return new Result("添加成功").addMap("restaurant", newRst);
    }

    /**
     * 商家 - 获取自己的店铺信息
     */
    @UserLoginToken
    @GetMapping("/restaurant")
    public Result getRestaurant() {
        return new Result("查询成功")
                .addMap("restaurant", restaurantService.getRstByShopper());
    }

    /**
     * 商家 - 更新自己的店铺信息
     */
    @UserLoginToken
    @PutMapping("/restaurant")
    public Result updateRestaurant(@Validated @RequestBody Restaurant restaurant) throws IOException {
        restaurantService.updateRestaurant(restaurant);
        restaurant = restaurantService.getRstByShopper();
        return new Result("更新成功").addMap("restaurant", restaurant);
    }

    /**
     * 获取指定店铺的基本信息
     */
    @GetMapping("/rst/{rstId}")
    public Result getRstDetail(@PathVariable("rstId") Long rstId) {
        Restaurant rstDetail = restaurantService.getRstDetail(rstId);
        return new Result("获取成功").addMap("rst", rstDetail);
    }

    /**
     * 获取周边店铺信息
     */
    @GetMapping("/nearby/rsts")
    public Result getNearbyRstRemark(@RequestParam("type") int type,
                                     @RequestParam("lon") double lon,
                                     @RequestParam("lat") double lat,
                                     @RequestParam("from") int from,
                                     @RequestParam("size") int size) throws IOException {
        List<Map<String, Object>> rstList = restaurantService.getRstRemark(type, lon, lat, from, size);
        return new Result("获取成功").addMap("rsts", rstList);
    }

    /**
     * 查询周边店铺信息
     */
    @GetMapping("/nearby/rsts/filter")
    public Result getRstRemarkByKeyword(@RequestParam("keyword") String keyword,
                                        @RequestParam("type") int type,
                                        @RequestParam("lon") double lon,
                                        @RequestParam("lat") double lat,
                                        @RequestParam("from") int from,
                                        @RequestParam("size") int size) throws IOException {
        List<Map<String, Object>> rstList = restaurantService.getRstRemarkWithSearch(keyword, type, lon, lat, from, size);
        return new Result("获取成功").addMap("rsts", rstList);
    }


}
