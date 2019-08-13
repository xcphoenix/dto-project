package com.xcphoenix.dto.mapper;

import com.xcphoenix.dto.bean.City;

import java.util.List;

/**
 * @author      xuanc
 * @date        2019/8/12 下午5:28
 * @version     1.0
 */ 
public interface CityMapper {

    List<City> selectByProvinceId(String provinceId);

}
