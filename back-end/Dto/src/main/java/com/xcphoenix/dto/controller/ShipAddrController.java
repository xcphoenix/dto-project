package com.xcphoenix.dto.controller;

import com.xcphoenix.dto.annotation.UserLoginToken;
import com.xcphoenix.dto.bean.ShipAddr;
import com.xcphoenix.dto.result.Result;
import com.xcphoenix.dto.service.ShipAddrService;
import com.xcphoenix.dto.validator.ValidateGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author      xuanc
 * @date        2019/9/5 下午5:00
 * @version     1.0
 */
@RestController
@RequestMapping("/user")
public class ShipAddrController {

    private ShipAddrService shipAddrService;

    @Autowired
    public ShipAddrController(ShipAddrService shipAddrService) {
        this.shipAddrService = shipAddrService;
    }

    @UserLoginToken
    @PostMapping("/address")
    public Result addShipAddr(@Validated(ValidateGroup.addData.class) @RequestBody ShipAddr shipAddr) {
        ShipAddr shipAddrAdded = shipAddrService.addShipAddr(shipAddr);
        return new Result("添加成功").addMap("address", shipAddrAdded);
    }

    @UserLoginToken
    @PutMapping("/address/{shipAddrId}")
    public Result updateShipAddr(@Validated @RequestBody ShipAddr shipAddr, @PathVariable Integer shipAddrId) {
        shipAddr.setShipAddrId(shipAddrId);
        ShipAddr shipAddrUpd = shipAddrService.updateShipAddr(shipAddr);
        return new Result("更新成功").addMap("address", shipAddrUpd);
    }

    @UserLoginToken
    @DeleteMapping("/address/{shipAddrId}")
    public Result delShipAddr(@PathVariable Integer shipAddrId) {
        shipAddrService.delShipAddr(shipAddrId);
        return new Result("删除成功");
    }

    @UserLoginToken
    @GetMapping("/address/{shipAddrId}")
    public Result getShipAddrById(@PathVariable Integer shipAddrId) {
        ShipAddr shipAddr = shipAddrService.getAddrMsgById(shipAddrId);
        return new Result("获取成功").addMap("address", shipAddr);
    }

    @UserLoginToken
    @GetMapping("/addresses")
    public Result getShipAddresses() {
        List<ShipAddr> shipAddrList = shipAddrService.getAddresses();
        return new Result("获取成功").addMap("addresses", shipAddrList);
    }

}
