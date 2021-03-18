package com.github.kaellybot.kaellyhorn.command.sound;

import com.github.kaellybot.kaellyhorn.model.constant.Language;
import com.github.kaellybot.kaellyhorn.command.util.AbstractCommandArgument;
import com.github.kaellybot.kaellyhorn.command.util.Command;
import com.github.kaellybot.kaellyhorn.util.DiscordTranslator;
import com.github.kaellybot.kaellyhorn.util.GuildAudioManager;
import com.github.kaellybot.kaellyhorn.util.SoundUtils;
import com.github.kaellybot.kaellyhorn.util.annotation.Described;
import discord4j.common.util.Snowflake;
import discord4j.core.object.VoiceState;
import discord4j.core.object.entity.Member;
import discord4j.core.object.entity.Message;
import discord4j.voice.AudioProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.regex.Matcher;

import static com.github.kaellybot.kaellyhorn.model.constant.Constants.PLAYER_MANAGER;

@Slf4j
@Described
@Component
@Qualifier(SoundCommand.COMMAND_QUALIFIER)
public class SoundNoArgument extends AbstractCommandArgument {

    private final SoundUtils soundUtils;

    public SoundNoArgument(@Qualifier(SoundCommand.COMMAND_QUALIFIER) Command parent, SoundUtils soundUtils, DiscordTranslator translator) {
        super(parent, translator);
        this.soundUtils = soundUtils;
    }

    @Override
    public Flux<Message> execute(Message message, String prefix, Language language, Matcher matcher) {
        final GuildAudioManager manager = GuildAudioManager.of(message.getGuildId().orElse(Snowflake.of(0)));
        final AudioProvider provider = manager.getProvider();

        return soundUtils.getRandomSound().map(sound -> {
            PLAYER_MANAGER.loadItemOrdered(manager, sound, soundUtils.getAudioResultHandler(manager));
            return message.getAuthorAsMember()
                    .doOnError(e -> log.error(e.getMessage()))
                    .flatMap(Member::getVoiceState)
                    .doOnError(e -> log.error(e.getMessage()))
                    .flatMap(VoiceState::getChannel)
                    .doOnError(e -> log.error(e.getMessage()))
                    .flatMap(channel -> soundUtils.playSound(channel, provider))
                    .doOnError(e -> log.error(e.getMessage()))
                    .thenReturn(message);
        }).orElse(message.getChannel()
                .flatMap(chan -> chan.createMessage(getTranslator().getLabel(language, "sound.no_sound"))))
                .flatMapMany(Flux::just);
    }

    @Override
    public String help(Language lg, String prefix) {
        return prefix + getTranslator().getLabel(lg, "sound.help.no_argument");
    }
}
