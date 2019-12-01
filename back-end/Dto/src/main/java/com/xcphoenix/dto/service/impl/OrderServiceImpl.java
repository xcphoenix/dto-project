package com.xcphoenix.dto.service.impl;

import com.xcphoenix.dto.bean.ao.OrderModify;
import com.xcphoenix.dto.bean.bo.DeliveryType;
import com.xcphoenix.dto.bean.bo.OrderStatusEnum;
import com.xcphoenix.dto.bean.bo.PayTypeEnum;
import com.xcphoenix.dto.bean.dao.*;
import com.xcphoenix.dto.exception.ServiceLogicException;
import com.xcphoenix.dto.mapper.OrderMapper;
import com.xcphoenix.dto.result.ErrorCode;
import com.xcphoenix.dto.service.*;
import com.xcphoenix.dto.service.job.JobService;
import com.xcphoenix.dto.utils.ContextHolderUtils;
import com.xcphoenix.dto.utils.SnowFlakeUtils;
import org.quartz.SchedulerException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

import static com.xcphoenix.dto.bean.bo.OrderStatusEnum.NEED_PAY;

/**
 * @author      xuanc
 * @date        2019/10/23 下午3:37
 * @version     1.0
 */
@Service
public class OrderServiceImpl implements OrderService {

    private OrderMapper orderMapper;
    private RestaurantService restaurantService;
    private FoodService foodService;
    private ShipAddrService shipAddrService;
    private CartService cartService;
    private JobService jobService;
    private StockService stockService;
    private DoBusinessService doBusinessService;

    public OrderServiceImpl(OrderMapper orderMapper, RestaurantService restaurantService,
                            FoodService foodService, ShipAddrService shipAddrService,
                            CartService cartService, JobService jobService,
                            StockService stockService, DoBusinessService doBusinessService) {
        this.orderMapper = orderMapper;
        this.restaurantService = restaurantService;
        this.foodService = foodService;
        this.shipAddrService = shipAddrService;
        this.cartService = cartService;
        this.jobService = jobService;
        this.stockService = stockService;
        this.doBusinessService = doBusinessService;
    }

    /**
     * 购物车商品信息转订单对象
     */
    private OrderItem toOrderItem(CartItem cartItem) {
        OrderItem abstractOrderItem = new OrderItem(cartItem);
        abstractOrderItem.setExFoodName(foodService
                .getFoodDetailById(cartItem.getFoodId())
                .getName());
        abstractOrderItem.setExFoodImgUrl(foodService
                .getFoodDetailById(cartItem.getFoodId())
                .getCoverImg());
        return abstractOrderItem;
    }

    /**
     * 购物车信息转化为订单初始信息
     * @param cart 购物车
     * @param order 订单信息（为空创建新对象，不为空只设置值）
     * @return 订单信息
     * @throws com.xcphoenix.dto.exception.ServiceLogicException 店铺不存在或订单不满足配送条件
     */
    private Order toOrder(Cart cart, Order order) {
        Restaurant rst = getConditionedRst(cart);
        if (order == null) {
            order = new Order();
            // 设置默认支付方式
            order.setPayType(PayTypeEnum.defaultValue());
            // 设置默认配送方式
            order.setDeliveryType(DeliveryType.SHOP_SELF_SUPPORT.getValue());
        }
        order.setUserId(cart.getUserId());
        order.setRstId(cart.getRestaurantId());
        order.setDiscountAmount(BigDecimal.valueOf(cart.getDiscountAmount()));
        order.setOriginalPrice(BigDecimal.valueOf(cart.getOriginalTotal()));
        order.setTotalPrice(BigDecimal.valueOf(cart.getTotal()));
        order.setItemCount(cart.getTotalWeight());
        order.setPackagePrice(BigDecimal.valueOf(rst.getPackagePrice()));
        order.setDeliveryPrice(BigDecimal.valueOf(rst.getDeliveryPrice()));

        List<OrderItem> orderItemList = new ArrayList<>();

        for (CartItem cartItem : cart.getCartItems()) {
            OrderItem orderItem = toOrderItem(cartItem);
            orderItemList.add(orderItem);
        }
        order.setOrderItems(orderItemList);

        // extra rst
        order.setExRstName(rst.getRestaurantName());
        order.setExRstLogoUrl(rst.getLogo());
        // extra ship
        ShipAddr shipAddr = order.getShipAddrId() == null ? shipAddrService.getDefaultAddress()
                : shipAddrService.getAddrMsgById(order.getShipAddrId());
        if (shipAddr != null) {
            order.setExUserName(shipAddr.getContact());
            order.setExUserPhone(shipAddr.getPhone());
            order.setExShipAddr(shipAddr.getAddress() + " " + shipAddr.getAddressDetail());
            order.setShipAddrId(shipAddr.getShipAddrId());
        }
        return order;
    }

    /**
     * 数据校验
     */
    private void assertConditionAttrs(Order order) throws ServiceLogicException {
        // 数据完整性：支付方式、收货地址、配送方式
        if (order == null || PayTypeEnum.isMatched(order.getPayType()) ||
                order.getShipAddrId() == null || !DeliveryType.isMatched(order.getDeliveryType())) {
            throw new ServiceLogicException(ErrorCode.ORDER_NOT_CONDITIONAL);
        }
    }

    /**
     * 下单要求检测
     * 返回店铺数据
     */
    private Restaurant getConditionedRst(Cart cart) throws ServiceLogicException {
        if (cart == null) {
            throw new ServiceLogicException(ErrorCode.ORDER_NOT_CONDITIONAL);
        }
        Restaurant rst = restaurantService.getRstDetail(cart.getRestaurantId());
        if (rst == null) {
            throw new ServiceLogicException(ErrorCode.SHOP_NOT_FOUND);
        }

        // 配送要求
        if (cart.getTotal() < rst.getMinPrice()) {
            throw new ServiceLogicException(ErrorCode.ORDER_NOT_CONDITIONAL);
        }

        // 库存要求
        Map<Long, Integer> baseData = cart.getCartItems().stream().collect(
                Collectors.toMap(CartItem::getFoodId, CartItem::getQuantity)
        );
        Set<Long> outOfStockFoodIds = foodService.filterFoodsOfLackStock(rst.getRestaurantId(), baseData);
        if (outOfStockFoodIds.size() != 0) {
            throw new ServiceLogicException(ErrorCode.OUT_OF_STOCK, outOfStockFoodIds.toArray());
        }

        return rst;
    }

    /**
     * 更新订单状态
     */
    private void updateOrderStatus(Long orderCode, OrderStatusEnum orderStatusEnum) {
        orderMapper.changeOrderStatus(ContextHolderUtils.getLoginUserId(), orderCode, orderStatusEnum.getValue());
    }

    @Override
    public Order purchaseNewOrder(Order order) throws SchedulerException {
        Long userId = ContextHolderUtils.getLoginUserId();
        Long rstId = order.getRstId();
        Long orderCode = SnowFlakeUtils.nextId();

        order.setOrderCode(orderCode);
        order.setOrderTime(new Timestamp(System.currentTimeMillis()));
        /// 无用字段
        order.setInvalidTime(new Timestamp(System.currentTimeMillis() + 1000 * 15 * 60));
        // 设置默认配送方式
        order.setDeliveryType(DeliveryType.SHOP_SELF_SUPPORT.getValue());

        order = toOrder(cartService.getCart(userId, rstId), order);
        order.setStatus(NEED_PAY.getValue());
        assertConditionAttrs(order);

        // 添加订单数据、订单商品项数据
        orderMapper.addOrder(order);
        for (OrderItem item : order.getOrderItems()) {
            orderMapper.addOrderItem(item, orderCode);
        }
        // 库存处理
        stockService.lock(order);
        // 添加定时任务
        jobService.addJob(order.getOrderCode());

        return order;
    }

    @Override
    public Map<String, Object> getPreviewData(Long rstId) {
        Long userId = ContextHolderUtils.getLoginUserId();
        Order order = toOrder(cartService.getCart(userId, rstId),null);
        ShipAddr shipAddr = shipAddrService.getDefaultAddress();

        Map<String, Object> map = new HashMap<>(2);
        map.put("order", order);
        map.put("address", shipAddr);
        map.put("payType", PayTypeEnum.values());
        map.put("deliveryType", DeliveryType.values());

        return map;
    }

    @Override
    public void cancelOrder(Long orderCode) throws ServiceLogicException {
        int status = getOrderStatus(orderCode);
        if (!NEED_PAY.match(status)) {
            throw new ServiceLogicException(ErrorCode.ORDER_STATUS_EXCEPTIONAL);
        }
        updateOrderStatus(orderCode, OrderStatusEnum.CANCEL);
        stockService.resume(getOrderById(orderCode));
    }

    @Override
    public void modifyOrder(Long orderCode, OrderModify modData) throws ServiceLogicException {
        int status = getOrderStatus(orderCode);
        if (!NEED_PAY.match(status)) {
            throw new ServiceLogicException(ErrorCode.ORDER_STATUS_EXCEPTIONAL);
        }
        orderMapper.updateOrder(ContextHolderUtils.getLoginUserId(), orderCode, modData);
    }

    @Override
    public void delOrder(Long orderCode) throws ServiceLogicException {
        int orderStatus = getOrderStatus(orderCode);
        if (!OrderStatusEnum.TIMEOUT.match(orderStatus) || !OrderStatusEnum.CANCEL.match(orderStatus)) {
            throw new ServiceLogicException(ErrorCode.ORDER_STATUS_EXCEPTIONAL);
        }
        orderMapper.delCanceledOrder(ContextHolderUtils.getLoginUserId(), orderCode);
        if (NEED_PAY.match(orderStatus)) {
            stockService.resume(getOrderById(orderCode));
        }
    }

    @Override
    public boolean isValid(Long orderCode) {
        return orderMapper.getOrderStatus(ContextHolderUtils.getLoginUserId(), orderCode) != null;
    }

    @Override
    public int getOrderStatus(Long orderCode) {
        return orderMapper.getOrderStatus(ContextHolderUtils.getLoginUserId(), orderCode);
    }

    @Override
    public Order getOrderById(Long orderCode) {
        return orderMapper.getOrderById(ContextHolderUtils.getLoginUserId(), orderCode);
    }

    @Override
    public void dealOrderTimeout(Long orderCode) {
        // 订单过期
        updateOrderStatus(orderCode, OrderStatusEnum.TIMEOUT);
        // 恢复库存
        stockService.resume(getOrderById(orderCode));
    }

    @Override
    public void dealOrderPaid(Long orderCode, int payType) {
        // 更新订单状态
        updateOrderStatus(orderCode, OrderStatusEnum.WAIT_SHOPPER);
        // 同步销量等信息
        doBusinessService.syncOrdered(getOrderById(orderCode));
    }

}
