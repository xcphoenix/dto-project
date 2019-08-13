package com.xcphoenix.dto.mapper;

import com.xcphoenix.dto.bean.Country;

import java.util.List;

/**
 * @author      xuanc
 * @date        2019/8/12 下午5:29
 * @version     1.0
 */ 
public interface CountryMapper {

    List<Country> selectByCityId(String cityId);

}
