package com.xcphoenix.dto;

import com.alibaba.fastjson.JSON;
import com.xcphoenix.dto.mapper.CollectionMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 * @author      xuanc
 * @date        2019/10/2 下午5:15
 * @version     1.0
 */
@Slf4j
@ExtendWith(SpringExtension.class)
@SpringBootTest
class CollectTest {

    @Autowired
    private CollectionMapper collectionMapper;

    @Test
    void getCollectedShopTest() {
        log.info(JSON.toJSONString(collectionMapper.getCollectedShops(2)));
    }

}
