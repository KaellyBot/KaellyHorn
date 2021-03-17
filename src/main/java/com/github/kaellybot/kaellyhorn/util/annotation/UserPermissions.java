package com.github.kaellybot.kaellyhorn.util.annotation;

import com.github.kaellybot.kaellyhorn.model.constant.PermissionScope;

import java.lang.annotation.*;

/**
 * This annotation is used to detect if the user who triggered the bot has enough permissions to continue the process
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Inherited
@Documented
public @interface UserPermissions {
    PermissionScope value();
}
