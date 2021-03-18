package com.github.kaellybot.kaellyhorn.command.sound;

import com.github.kaellybot.kaellyhorn.command.util.CommandArgument;
import com.github.kaellybot.kaellyhorn.command.util.AbstractCommand;
import com.github.kaellybot.kaellyhorn.model.constant.Language;
import com.github.kaellybot.kaellyhorn.util.DiscordTranslator;
import com.github.kaellybot.kaellyhorn.util.SoundUtils;
import discord4j.core.object.entity.Message;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Qualifier(SoundCommand.COMMAND_QUALIFIER)
public class SoundCommand extends AbstractCommand {

    public static final String COMMAND_QUALIFIER = "SoundCommand";
    public static final String COMMAND_NAME = "sound";

    private final SoundUtils soundUtils;

    public SoundCommand(@Qualifier(COMMAND_QUALIFIER) @Lazy List<CommandArgument<Message>> arguments, SoundUtils soundUtils, DiscordTranslator translator) {
        super(COMMAND_NAME, arguments, translator);
        this.soundUtils = soundUtils;
    }

    @Override
    public String help(Language lg, String prefix){
        return "**" + prefix + name + "** " + translator
                .getLabel(lg, name.toLowerCase() + ".help", String.join(", ", soundUtils.getSoundList()));
    }
}