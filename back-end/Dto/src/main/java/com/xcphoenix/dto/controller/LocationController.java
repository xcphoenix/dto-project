package com.xcphoenix.dto.controller;

import com.alibaba.fastjson.JSONObject;
import com.xcphoenix.dto.annotation.PassToken;
import com.xcphoenix.dto.result.ErrorCode;
import com.xcphoenix.dto.result.Result;
import com.xcphoenix.dto.service.GeoService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author      xuanc
 * @date        2020/1/31 上午10:48
 * @version     1.0
 */
@RestController
@RequestMapping("/location")
public class LocationController {

    private GeoService geoService;

    public LocationController(GeoService geoService) {
        this.geoService = geoService;
    }

    @PassToken
    @GetMapping("/ip")
    public Result location() {
        JSONObject jsonObject = geoService.getLocation();

        if (jsonObject != null) {
            Map<String, Object> resultMap = jsonObject.getInnerMap();
            return new Result(null, resultMap);
        }
        return Result.error(ErrorCode.LOCATION_FAILED);
    }

    @PassToken
    @GetMapping("/suggestion")
    public Result suggestion(@RequestParam("keyword") String keyword) {
        JSONObject jsonObject = geoService.getSuggestion(keyword);
        return new Result(null, jsonObject.getInnerMap());
    }

}
