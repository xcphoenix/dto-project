package com.xcphoenix.dto;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.xcphoenix.dto.bean.ShipAddr;
import com.xcphoenix.dto.exception.ServiceLogicException;
import com.xcphoenix.dto.service.ShipAddrService;
import com.xcphoenix.dto.util.ContextHolderUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author      xuanc
 * @date        2019/9/3 下午11:00
 * @version     1.0
 */
@Slf4j
@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
@Rollback
@DisplayName("收货地址测试")
class ShipAddrTest {

    @Autowired
    private ShipAddrService shipAddrService;

    private Object userId = null;
    private ShipAddr shipAddr = new ShipAddr(
            null, 91L, "联系人", "18222222222", null, "110101", "xxx地址",
            BigDecimal.valueOf(110.088280), BigDecimal.valueOf(34.567000), null, null, 1, null, false
    );

    @BeforeEach
    void setUserId() {
        // save
        userId = ContextHolderUtils.getRequest().getAttribute("userId");
        ContextHolderUtils.getRequest().setAttribute("userId", 91L);
    }

    @AfterEach
    void resetUserId() {
        // reset
        ContextHolderUtils.getRequest().setAttribute("userId", userId);
    }

    @DisplayName("添加")
    @Test
    void testAddShipAddr() {
        log.info("add");

        // test add
        shipAddrService.addShipAddr(shipAddr);
        assertNotNull(shipAddr.getShipAddrId());
        assertNotNull(shipAddr.getGeohash());
        assertEquals(shipAddr.getTagName(), "家庭");

        // test get
        ShipAddr newShipAddr = shipAddrService.getAddrMsgById(shipAddr.getShipAddrId());
        assertNotNull(newShipAddr);
        log.info(JSON.toJSON(newShipAddr).toString(), SerializerFeature.PrettyFormat);
    }

    @DisplayName("更新")
    @Test
    void testUpdateAddr() {
        log.info("update");

        // add again
        shipAddrService.addShipAddr(shipAddr);

        // update new address
        shipAddrService.addShipAddr(shipAddr);
        shipAddr.setAddress("update address");
        shipAddr.setTagType(2);
        shipAddr.setPhone("13512341234");
        shipAddrService.updateShipAddr(shipAddr);

        // check
        ShipAddr newAddr = shipAddrService.getAddrMsgById(shipAddr.getShipAddrId());
        assertEquals(shipAddr.getPhone(), "13512341234");
        assertEquals(shipAddr.getAddress(), "update address");
        assertEquals(shipAddr.getTagType(), Integer.valueOf(2));
    }

    @DisplayName("批量获取＆删除")
    @Test
    void testGetsDelAddr() {
        log.info("get & del");

        List<ShipAddr> shipAddrList = shipAddrService.getAddresses();
        int beforeSize = shipAddrList.size();

        shipAddrService.addShipAddr(shipAddr);
        shipAddrService.addShipAddr(shipAddr);

        shipAddrList = shipAddrService.getAddresses();
        int afterSize = shipAddrList.size();

        assertNotNull(shipAddrList);
        assertEquals(afterSize - beforeSize, 2);

        Long firstShipAddrId = shipAddrList.get(0).getShipAddrId();

        shipAddrService.delShipAddr(firstShipAddrId);

        ServiceLogicException sle = assertThrows(ServiceLogicException.class,
                () -> shipAddrService.getAddrMsgById(firstShipAddrId));
        assertTrue(sle.getMessage().contains("收货地址不存在"));
        assertEquals(afterSize - 1, shipAddrService.getAddresses().size());
    }

}
