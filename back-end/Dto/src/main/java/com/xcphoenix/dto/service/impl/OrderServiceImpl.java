package com.xcphoenix.dto.service.impl;

import com.xcphoenix.dto.bean.*;
import com.xcphoenix.dto.exception.ServiceLogicException;
import com.xcphoenix.dto.mapper.OrderMapper;
import com.xcphoenix.dto.result.ErrorCode;
import com.xcphoenix.dto.service.*;
import com.xcphoenix.dto.utils.ContextHolderUtils;
import com.xcphoenix.dto.utils.SnowFlakeUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

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

    public OrderServiceImpl(OrderMapper orderMapper, RestaurantService restaurantService,
                            FoodService foodService, ShipAddrService shipAddrService, CartService cartService) {
        this.orderMapper = orderMapper;
        this.restaurantService = restaurantService;
        this.foodService = foodService;
        this.shipAddrService = shipAddrService;
        this.cartService = cartService;
    }

    /**
     * 购物车商品信息转订单对象
     */
    private OrderItem toOrderItem(CartItem cartItem) {
        OrderItem abstractOrderItem = new OrderItem(cartItem);
        abstractOrderItem.setExFoodName(foodService
                .getFoodDetailById(cartItem.getFoodId())
                .getName());
        abstractOrderItem.setExFoodName(foodService
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
            order.setPayType(PayTypeEnum.defaultId());
        }
        order.setUserId(cart.getUserId());
        order.setRstId(cart.getRestaurantId());
        order.setDiscountAmount(BigDecimal.valueOf(cart.getDiscountAmount()));
        order.setOriginalPrice(BigDecimal.valueOf(cart.getOriginalTotal()));
        order.setTotalPrice(BigDecimal.valueOf(cart.getTotal()));
        order.setItemCount(cart.getTotalWeight());

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
        // 数据完整性：支付方式、收货地址
        if (order == null || !PayTypeEnum.includeId(order.getPayType()) || order.getShipAddrId() == null) {
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
     * 下单
     * @param order 支付方式、收货地址、订单备注
     */
    @Override
    public Order purchaseNewOrder(Order order) {
        Long userId = ContextHolderUtils.getLoginUserId();
        Long rstId = order.getRstId();

        order.setOrderCode(SnowFlakeUtils.nextId());
        order.setOrderTime(new Timestamp(System.currentTimeMillis()));
        order.setInvalidTime(new Timestamp(System.currentTimeMillis() + 1000 * 10 * 60));

        order = toOrder(cartService.getCart(userId, rstId), order);
        order.setStatus(OrderStatusEnum.NEED_PAY.getId());
        assertConditionAttrs(order);

        // TODO Redis + DelayQueue 处理订单 + 库存处理(减少&释放)
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
        return map;
    }

    public boolean isValid(Long orderCode) {
        return orderMapper.getOrderStatus(ContextHolderUtils.getLoginUserId(), orderCode) != null;
    }

}
