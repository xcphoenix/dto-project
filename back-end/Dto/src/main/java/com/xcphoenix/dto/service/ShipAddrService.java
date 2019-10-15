package com.xcphoenix.dto.service;

import com.xcphoenix.dto.bean.ShipAddr;

import java.util.List;

/**
 * @author xuanc
 * @version 1.0
 * @date 2019/9/3 下午10:00
 */
public interface ShipAddrService {

    /**
     * 添加收货地址
     *
     * @param shipAddr 收货地址信息
     * @return 添加成功后收货地址信息
     */
    ShipAddr addShipAddr(ShipAddr shipAddr);

    /**
     * 删除收货地址
     *
     * @param shipAddrId 收货地址id
     */
    void delShipAddr(Long shipAddrId);

    /**
     * 更新收货地址信息
     *
     * @param shipAddr 收货地址信息
     * @return 被更新的收货地址的所有信息
     */
    ShipAddr updateShipAddr(ShipAddr shipAddr);

    /**
     * 获取指定id的地址信息
     *
     * @param shipAddrId 地址id
     * @return 地址信息
     */
    ShipAddr getAddrMsgById(Long shipAddrId);

    /**
     * 获取用户所设置的收货地址信息
     *
     * @return 收货地址信息列表
     */
    List<ShipAddr> getAddresses();
}
