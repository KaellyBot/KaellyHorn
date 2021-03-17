package com.github.kaellybot.kaellyhorn.util.annotation;

import com.github.kaellybot.kaellyhorn.model.constant.Order;

import java.lang.annotation.*;

/**
 * This annotation is used to sort the command argument when displayed by the help argument
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Inherited
@Documented
public @interface DisplayOrder {
    Order value();
}