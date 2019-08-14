package com.xcphoenix.dto.bean.area;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author xuanc
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Country {

    @JSONField(name = "value")
    int id;

    @JSONField(name = "label")
    String name;

    @JSONField(serialize = false)
    String countryId;

    @JSONField(serialize = false)
    String cityId;

}
