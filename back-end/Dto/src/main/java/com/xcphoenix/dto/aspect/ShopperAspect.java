package com.xcphoenix.dto.aspect;

import com.xcphoenix.dto.annotation.ShopperCheck;
import com.xcphoenix.dto.exception.ServiceLogicException;
import com.xcphoenix.dto.result.ErrorCode;
import com.xcphoenix.dto.service.RestaurantService;
import com.xcphoenix.dto.util.ContextHolderUtils;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author xuanc
 * @version 1.0
 * @date 2019/8/14 下午4:42
 */
@Aspect
@Component
public class ShopperAspect {

    private RestaurantService restaurantService;
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public ShopperAspect(RestaurantService restaurantService) {
        this.restaurantService = restaurantService;
    }

    /**
     * 若注解仅在类上(方法上没有), 则可通过@within拦截到, 此时通过方法没有拦截到, logParameter为null
     * 若注解在方法上, 则可以通过@annotation拦截到, 此时logParameter为方法上加的注解
     */
    @Pointcut(value = "@within(shopperCheck) || @annotation(shopperCheck)",
            argNames = "shopperCheck")
    public void checkShopper(ShopperCheck shopperCheck) {
    }

    @Before(value = "checkShopper(shopperCheck)", argNames = "shopperCheck")
    public void execBeforePointcut(ShopperCheck shopperCheck) {
        Integer userId = (Integer) ContextHolderUtils.getRequest().getAttribute("userId");
        logger.info("验证用户是否是商家..");
        if (userId == null) {
            throw new ServiceLogicException(ErrorCode.SERVER_EXCEPTION);
        }
        if (shopperCheck == null || !shopperCheck.enable()) {
            return;
        }
        if (!restaurantService.isNewShopper(userId)) {
            throw new ServiceLogicException(ErrorCode.USER_NOT_SHOPPER);
        }
    }
}
