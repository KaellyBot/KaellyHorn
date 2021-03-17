package com.github.kaellybot.kaellyhorn.util.annotation;

import com.github.kaellybot.kaellyhorn.model.constant.PermissionScope;

import java.lang.annotation.*;

/**
 * This annotation is used to detect if the bot has the good authorization in a specific guild to perform task like send a message or create a webhook.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Inherited
@Documented
public @interface BotPermissions {
    PermissionScope value();
}
