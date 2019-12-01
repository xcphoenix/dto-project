package com.xcphoenix.dto.controller.test;

import com.xcphoenix.dto.bean.bo.DeliveryType;
import com.xcphoenix.dto.bean.bo.PayTypeEnum;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @author      xuanc
 * @date        2019/12/1 下午3:41
 * @version     1.0
 */
@RestController
@RequestMapping("/test")
public class TestController {

    @GetMapping("/enum")
    public Map<String, Object> testEnumSerialize() {
        Map<String, Object> map = new HashMap<>(4);
        map.put("name1", 1);
        map.put("name2", "value2");
        map.put("payType", PayTypeEnum.values());
        map.put("deliveryType", DeliveryType.values());
        return map;
    }

}
