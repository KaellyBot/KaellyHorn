package com.github.kaellybot.kaellyhorn.command.sound;

import com.github.kaellybot.kaellyhorn.model.constant.Language;
import com.github.kaellybot.kaellyhorn.command.util.AbstractCommandArgument;
import com.github.kaellybot.kaellyhorn.command.util.Command;
import com.github.kaellybot.kaellyhorn.util.DiscordTranslator;
import com.github.kaellybot.kaellyhorn.util.GuildAudioManager;
import com.github.kaellybot.kaellyhorn.util.SoundUtils;
import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import discord4j.common.util.Snowflake;
import discord4j.core.object.VoiceState;
import discord4j.core.object.entity.Member;
import discord4j.core.object.entity.Message;
import discord4j.voice.AudioProvider;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import java.util.regex.Matcher;

import static com.github.kaellybot.kaellyhorn.model.constant.Constants.PLAYER_MANAGER;

@Component
@Qualifier(SoundCommand.COMMAND_QUALIFIER)
public class SoundNoArgument extends AbstractCommandArgument {

    public SoundNoArgument(@Qualifier(SoundCommand.COMMAND_QUALIFIER) Command parent, DiscordTranslator translator) {
        super(parent, translator);
    }

    @Override
    public Flux<Message> execute(Message message, String prefix, Language language, Matcher matcher) {
        final GuildAudioManager manager = GuildAudioManager.of(message.getGuildId().orElse(Snowflake.of(0)));
        final AudioProvider provider = manager.getProvider();
        PLAYER_MANAGER.loadItemOrdered(manager, "https://www.youtube.com/watch?v=dQw4w9WgXcQ",
                new AudioLoadResultHandler() {

                    @Override
                    public void trackLoaded(AudioTrack audioTrack) {

                    }

                    @Override
                    public void playlistLoaded(AudioPlaylist audioPlaylist) {

                    }

                    @Override
                    public void noMatches() {

                    }

                    @Override
                    public void loadFailed(FriendlyException e) {

                    }
                });

        return message.getAuthorAsMember()
                .flatMap(Member::getVoiceState)
                .flatMap(VoiceState::getChannel)
                .flatMap(channel ->  SoundUtils.playSound(channel, provider))
                .thenReturn(message)
                .flatMapMany(Flux::just);
    }
}
