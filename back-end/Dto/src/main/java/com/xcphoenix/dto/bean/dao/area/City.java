package com.xcphoenix.dto.bean.dao.area;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author      xuanc
 * @date        2019/8/12 下午5:14
 * @version     1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class City {

    @JSONField(name = "value")
    int id;

    @JSONField(name = "label")
    String name;

    @JSONField(serialize = false)
    String cityId;

    @JSONField(serialize = false)
    String provinceId;

    @JSONField(name = "children")
    List<Country> countries;

}
