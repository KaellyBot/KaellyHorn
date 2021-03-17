package com.github.kaellybot.kaellyhorn.model.error;

import com.github.kaellybot.kaellyhorn.model.constant.Error;
import com.github.kaellybot.kaellyhorn.model.constant.Language;
import com.github.kaellybot.kaellyhorn.command.util.Command;
import com.github.kaellybot.kaellyhorn.util.DiscordTranslator;
import discord4j.rest.util.Permission;
import lombok.AllArgsConstructor;

import java.util.Set;
import java.util.stream.Collectors;

@AllArgsConstructor
public class MissingBotPermissionError implements Error {

    private final Command command;
    private final Set<Permission> permissions;

    @Override
    public String getLabel(DiscordTranslator translator, Language language){
        return permissions.stream()
                .map(permission -> translator.getLabel(language, "permission." + permission.name().toLowerCase()))
                .collect(Collectors.joining(", ", translator
                        .getLabel(language, "error.missing_bot_permission", command.getName()) + " ", "."));
    }
}
