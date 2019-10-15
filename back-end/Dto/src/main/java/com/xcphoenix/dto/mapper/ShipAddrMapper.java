package com.xcphoenix.dto.mapper;

import com.xcphoenix.dto.bean.ShipAddr;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author xuanc
 * @version 1.0
 * @date 2019/9/3 上午9:27
 */
public interface ShipAddrMapper {

    /**
     * 添加收货地址
     *
     * @param shipAddr 收货地址信息
     */
    void addShipAddr(ShipAddr shipAddr);

    /**
     * 删除收货地址
     *
     * @param addrId 地址id
     * @param userId 用户id
     * @return 是否成功
     */
    int delShipAddr(@Param("addrId") Long addrId, @Param("userId") Long userId);

    /**
     * 更新收货地址
     *
     * @param shipAddr 更新后的信息
     */
    void updateShipAddr(ShipAddr shipAddr);

    /**
     * 获取指定用户的收货地址信息
     *
     * @param userId 用户id
     * @return 信息
     */
    List<ShipAddr> getAddrByUserId(@Param("userId") Long userId);

    /**
     * 获取指定 id 的收货地址信息
     *
     * @param userId 用户id
     * @param shipAddrId 收货地址id
     * @return 收货地址信息
     */
    ShipAddr getAddrById(@Param("shipAddrId") Long shipAddrId, @Param("userId") Long userId);

}
