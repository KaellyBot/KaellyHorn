package com.github.kaellybot.kaellyhorn.util;

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
}
