package com.github.kaellybot.kaellyhorn.command.sound;

import com.github.kaellybot.kaellyhorn.command.util.CommandArgument;
import com.github.kaellybot.kaellyhorn.command.util.AbstractCommand;
import com.github.kaellybot.kaellyhorn.util.DiscordTranslator;
import discord4j.core.object.entity.Message;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
@Qualifier(SoundCommand.COMMAND_QUALIFIER)
public class SoundCommand extends AbstractCommand {

    public static final String COMMAND_QUALIFIER = "SoundCommand";
    public static final String COMMAND_NAME = "sound";

    public SoundCommand(@Qualifier(COMMAND_QUALIFIER) @Lazy List<CommandArgument<Message>> arguments, DiscordTranslator translator) {
        super(COMMAND_NAME, arguments, translator);
    }
}