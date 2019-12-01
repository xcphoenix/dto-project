package com.xcphoenix.dto.mapper;

import com.xcphoenix.dto.bean.dao.OrderItem;
import org.apache.ibatis.annotations.Param;

/**
 * @author xuanc
 * @version 1.0
 * @date 2019/11/25 上午10:54
 */
public interface StockMapper {

    /**
     * 占用库存（单个商品）
     *
     * @param item 要占用的库存数据
     */
    void lock(@Param("item") OrderItem item);

    /**
     * 恢复库存占用
     *
     * @param item 要恢复的库存元素
     */
    void resume(@Param("item") OrderItem item);

}
