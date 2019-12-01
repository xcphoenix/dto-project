package com.xcphoenix.dto.service.impl;

import com.xcphoenix.dto.bean.dao.Order;
import com.xcphoenix.dto.bean.dao.OrderItem;
import com.xcphoenix.dto.mapper.DoBusinessMapper;
import com.xcphoenix.dto.service.DoBusinessService;
import org.springframework.stereotype.Service;

/**
 * 营业处理
 *
 * @author      xuanc
 * @date        2019/11/28 下午2:09
 * @version     1.0
 */
@Service
public class DoBusinessServiceImpl implements DoBusinessService {

    private DoBusinessMapper doBusinessMapper;

    public DoBusinessServiceImpl(DoBusinessMapper doBusinessMapper) {
        this.doBusinessMapper = doBusinessMapper;
    }

    @Override
    public void syncOrdered(Order order) {
        for (OrderItem orderItem : order.getOrderItems()) {
            doBusinessMapper.syncFood(orderItem);
        }
        doBusinessMapper.syncRst(order);
    }

}
