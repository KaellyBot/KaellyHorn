package com.github.kaellybot.kaellyhorn.model.error;

import com.github.kaellybot.kaellyhorn.command.sound.SoundCommand;
import com.github.kaellybot.kaellyhorn.model.constant.Error;
import com.github.kaellybot.kaellyhorn.model.constant.Language;
import com.github.kaellybot.kaellyhorn.command.util.Command;
import com.github.kaellybot.kaellyhorn.util.DiscordTranslator;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class MisusedCommandError implements Error {

    private final String prefix;
    private final Command command;

    @Override
    public String getLabel(DiscordTranslator translator, Language language){
        return translator.getLabel(language, "error.misused_command", prefix + SoundCommand.COMMAND_NAME + " help");
    }
}