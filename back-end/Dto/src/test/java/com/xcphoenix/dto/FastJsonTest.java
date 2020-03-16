package com.xcphoenix.dto;

import com.alibaba.fastjson.JSONException;
import com.xcphoenix.dto.utils.es.SearchRst;
import org.junit.jupiter.api.Test;

import java.io.IOException;

/**
 * @author      xuanc
 * @date        2020/3/15 下午11:01
 * @version     1.0
 */ 
public class FastJsonTest {

    @Test
    void demo() throws JSONException, IOException {
        SearchRst.JsonObj jsonObj = SearchRst.JsonObj.ofResource("es/distanceField.json");
        System.out.println(jsonObj);
    }

}
