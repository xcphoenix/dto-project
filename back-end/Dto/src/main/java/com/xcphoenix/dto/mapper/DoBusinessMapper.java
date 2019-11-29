package com.xcphoenix.dto.mapper;

import com.xcphoenix.dto.bean.Order;
import com.xcphoenix.dto.bean.OrderItem;
import org.apache.ibatis.annotations.Param;

/**
 * 订单成功后 - 同步营业数据
 *
 * @author xuanc
 * @version 1.0
 * @date 2019/11/28 下午4:45
 */
public interface DoBusinessMapper {

    /**
     * 商品数据同步
     * @param orderItem 要同步的数据项
     */
    void syncFood(@Param("orderItem") OrderItem orderItem);

    /**
     * 店铺数据同步
     * @param order 要同步的数据
     */
    void syncRst(@Param("order") Order order);

}
