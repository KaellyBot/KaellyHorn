package com.github.kaellybot.kaellyhorn.model.error;

import com.github.kaellybot.kaellyhorn.model.constant.Error;
import com.github.kaellybot.kaellyhorn.command.util.Command;
import discord4j.rest.util.Permission;

import java.util.Set;

public final class ErrorFactory {

    private ErrorFactory(){}

    public static Error createMisusedCommandError(String prefix, Command command){
        return new MisusedCommandError(prefix, command);
    }

    public static Error createMissingNSFWOptionError(){
        return new MissingNSFWOptionError();
    }

    public static Error createMissingBotPermissionError(Command command, Set<Permission> permissions){
        return new MissingBotPermissionError(command, permissions);
    }

    public static Error createMissingUserPermissionError(Command command, Set<Permission> permissions){
        return new MissingUserPermissionError(command, permissions);
    }

    public static Error createUnknownError(){
        return new UnknownError();
    }
}
