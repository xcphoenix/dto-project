package com.xcphoenix.dto.service.impl;

import com.xcphoenix.dto.bean.Order;
import com.xcphoenix.dto.bean.OrderItem;
import com.xcphoenix.dto.mapper.StockMapper;
import com.xcphoenix.dto.service.StockService;
import org.springframework.stereotype.Service;

/**
 * @author      xuanc
 * @date        2019/11/28 下午1:39
 * @version     1.0
 */
@Service
public class StockServiceImpl implements StockService {

    private StockMapper stockMapper;

    public StockServiceImpl(StockMapper stockMapper) {
        this.stockMapper = stockMapper;
    }

    @Override
    public void resume(Order order) {
        for (OrderItem orderItem : order.getOrderItems()) {
            stockMapper.resume(orderItem);
        }
    }

    @Override
    public void lock(Order order) {
        for (OrderItem orderItem : order.getOrderItems()) {
            stockMapper.lock(orderItem);
        }
    }

}
