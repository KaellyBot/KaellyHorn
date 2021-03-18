package com.github.kaellybot.kaellyhorn.util;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import discord4j.core.event.domain.VoiceStateUpdateEvent;
import discord4j.core.object.VoiceState;
import discord4j.core.object.entity.channel.VoiceChannel;
import discord4j.voice.AudioProvider;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Mono;

import java.time.Duration;

public final class SoundUtils {

    private SoundUtils(){}

    public static Mono<Void> playSound(VoiceChannel channel, AudioProvider provider){
        return channel.join(spec -> spec.setProvider(provider))
        .flatMap(connection -> {
            final Publisher<Boolean> voiceStateCounter = channel.getVoiceStates()
                    .count()
                    .map(count -> 1L == count);
            final Mono<Void> onDelay = Mono.delay(Duration.ofSeconds(10L))
                    .filterWhen(ignored -> voiceStateCounter)
                    .switchIfEmpty(Mono.never())
                    .then();
            final Mono<Void> onEvent = channel.getClient().getEventDispatcher().on(VoiceStateUpdateEvent.class)
                    .filter(event -> event.getOld().flatMap(VoiceState::getChannelId).map(channel.getId()::equals).orElse(false))
                    .filterWhen(ignored -> voiceStateCounter)
                    .next()
                    .then();
            return Mono.firstWithSignal(onDelay, onEvent).then(connection.disconnect());
        });
    }

    public static AudioLoadResultHandler getAudioResultHandler(GuildAudioManager manager){
        return new AudioLoadResultHandler() {

            @Override
            public void trackLoaded(AudioTrack audioTrack) {
                manager.getScheduler().play(audioTrack);
            }

            @Override
            public void playlistLoaded(AudioPlaylist audioPlaylist) {
                audioPlaylist.getTracks().forEach(manager.getScheduler()::play);
            }

            @Override
            public void noMatches() {

            }

            @Override
            public void loadFailed(FriendlyException e) {
                manager.getScheduler().skip();
            }
        };
    }
}
