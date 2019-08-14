package com.xcphoenix.dto.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 注解：是否需要商家身份
 *
 * @author xuanc
 * @version 1.0
 * @date 2019/8/14 下午4:51
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ShopperCheck {
    boolean enable() default true;
}
