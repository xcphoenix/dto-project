package com.xcphoenix.dto.mapper;

import com.xcphoenix.dto.bean.Order;
import org.apache.ibatis.annotations.Param;

/**
 * @author xuanc
 * @version 1.0
 * @date 2019/10/22 下午9:23
 */
public interface OrderMapper {

    /**
     * 添加订单
     *
     * @param order 订单基本属性
     *              地址信息
     *              支付方式
     *              店铺id
     *              数量
     *              商品id 数量 价格
     *              费用
     *              备注
     */
    void addOrder(@Param("order") Order order);

    /**
     * 修改订单信息
     *
     * @param order 订单信息
     */
    void updateOrder(Order order);

    /**
     * 取消订单 - 未到前可以取消
     *
     * @param userId  用户id
     * @param orderId 订单id
     */
    void cancelOrder(Long userId, Long orderId);

    /**
     * 获取订单详情
     *
     * @param orderId 订单id
     * @param userId  用户id
     */
    Order getOrderById(Long userId, Long orderId);

    /**
     * 获取订单数量
     *
     * @param userId 用户id
     * @return 订单数量
     */
    Integer getOrderNum(Long userId);

    /**
     * 获取全部订单
     *
     * @param userId 用户id
     */
    void getAllOrders(Long userId);

    /**
     * 修改订单状态
     *
     * @param userId  用户id
     * @param orderId 订单id
     * @param type    类型
     */
    void changeOrderStatus(@Param("userId") Long userId, @Param("orderId") Long orderId, @Param("type") int type);

    /**
     * 获取订单状态
     *
     * @param userId  用户id
     * @param orderId 订单id
     * @return 订单状态
     */
    Integer getOrderStatus(@Param("userId") Long userId, @Param("orderId") Long orderId);

}
