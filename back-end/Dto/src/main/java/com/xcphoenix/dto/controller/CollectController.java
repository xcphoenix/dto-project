package com.xcphoenix.dto.controller;

import com.xcphoenix.dto.annotation.UserLoginToken;
import com.xcphoenix.dto.bean.Restaurant;
import com.xcphoenix.dto.result.Result;
import com.xcphoenix.dto.service.CollectionService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author      xuanc
 * @date        2019/10/2 下午10:23
 * @version     1.0
 */
@RestController
@RequestMapping("/collection")
public class CollectController {

    private CollectionService collectionService;

    public CollectController(CollectionService collectionService) {
        this.collectionService = collectionService;
    }

    @UserLoginToken
    @GetMapping("/{shopId}")
    public Result collectShop(@PathVariable("shopId") Long shopId) {
        collectionService.collectShop(shopId);
        return new Result("收藏成功");
    }

    @UserLoginToken
    @DeleteMapping("/{shopId}")
    public Result cancelShop(@PathVariable("shopId") Long shopId) {
        collectionService.cancelCollect(shopId);
        return new Result("取消成功");
    }

    @UserLoginToken
    @GetMapping("/status/{shopId}")
    public Result getCollectionStatus(@PathVariable("shopId") Long shopId) {
        Boolean status = collectionService.getCollectStatus(shopId);
        return new Result(null).addMap("status", status);
    }

    @UserLoginToken
    @GetMapping("/list")
    public Result getCollectedShops() {
        List<Restaurant> restaurants = collectionService.getUserCollection();
        return new Result("获取成功").addMap("list", restaurants);
    }

}
