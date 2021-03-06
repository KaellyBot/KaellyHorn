package com.github.kaellybot.kaellyhorn.util.annotation;

import java.lang.annotation.*;

/**
 * This annotation is used to describe if the command is hidden for users or not
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Inherited
@Documented
public @interface Hidden {
}
