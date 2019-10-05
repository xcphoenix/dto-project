package com.xcphoenix.dto.mapper.area;

import com.xcphoenix.dto.bean.area.Country;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author xuanc
 * @version 1.0
 * @date 2019/8/12 下午5:29
 */
public interface CountryMapper {

    List<Country> selectByCityId(String cityId);

    @Select("SELECT _id FROM country WHERE country_id = #{code}")
    Integer getCountryId(@Param("code") String code);

    @Select("SELECT country_id FROM country WHERE _id = #{id}")
    String getCountryCode(@Param("id") Integer id);

}
