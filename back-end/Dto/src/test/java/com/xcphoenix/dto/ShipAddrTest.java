package com.xcphoenix.dto;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.xcphoenix.dto.bean.ShipAddr;
import com.xcphoenix.dto.service.ShipAddrService;
import com.xcphoenix.dto.util.ContextHolderUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * @author      xuanc
 * @date        2019/9/3 下午11:00
 * @version     1.0
 */
@Slf4j
@Transactional
@Rollback
@ExtendWith(SpringExtension.class)
@SpringBootTest
class ShipAddrTest {

    @Autowired
    private ShipAddrService shipAddrService;

    @DisplayName("添加收货地址")
    @Test
    void testAddShipAddr() {
        // save
        Object userId = ContextHolderUtils.getRequest().getAttribute("userId");
        ContextHolderUtils.getRequest().setAttribute("userId", 2);

        ShipAddr shipAddr = new ShipAddr(
                null, 2, "联系人", "18222222222", null, "110101", "xxx地址",
                BigDecimal.valueOf(110.0882809986), BigDecimal.valueOf(34.5670002034), null, null, 1, null, false
        );
        // test add
        shipAddrService.addShipAddr(shipAddr);
        assertNotNull(shipAddr.getShipAddrId());
        assertNotNull(shipAddr.getGeohash());
        assertEquals(shipAddr.getTagName(), "家庭");

        // test get
        ShipAddr newShipAddr = shipAddrService.getAddrMsgById(shipAddr.getShipAddrId());
        assertNotNull(newShipAddr);
        log.info(JSON.toJSON(newShipAddr).toString(), SerializerFeature.PrettyFormat);

        // restore
        ContextHolderUtils.getRequest().setAttribute("userId", userId);
    }

}
