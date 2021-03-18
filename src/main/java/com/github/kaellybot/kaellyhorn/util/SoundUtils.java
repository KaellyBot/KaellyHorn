package com.github.kaellybot.kaellyhorn.util;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import discord4j.core.event.domain.VoiceStateUpdateEvent;
import discord4j.core.object.VoiceState;
import discord4j.core.object.entity.channel.VoiceChannel;
import discord4j.voice.AudioProvider;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Component
public class SoundUtils {

    private static final File[] EMPTY_SOUNDS = new File[0];

    private final String soundsLocation;

    public SoundUtils(@Value("${sounds.directory}") String soundsLocation){
        this.soundsLocation = soundsLocation;
    }

    public Mono<Void> playSound(VoiceChannel channel, AudioProvider provider){
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

    public AudioLoadResultHandler getAudioResultHandler(GuildAudioManager manager){
        return new AudioLoadResultHandler() {

            @Override
            public void trackLoaded(AudioTrack audioTrack) {
                manager.getScheduler().play(audioTrack, true);
            }

            @Override
            public void playlistLoaded(AudioPlaylist audioPlaylist) {
                audioPlaylist.getTracks().forEach(manager.getScheduler()::play);
            }

            @Override
            public void noMatches() {
                throw new UnsupportedOperationException();
            }

            @Override
            public void loadFailed(FriendlyException e) {
                manager.getScheduler().skip();
            }
        };
    }

    public Optional<String> getRandomSound() {
        File[] sounds = getSounds();
        if (sounds.length > 0) {
            try {
                return Optional.of(sounds[new Random().nextInt(sounds.length)].getCanonicalPath());
            } catch (IOException e) {
                log.error(e.getMessage());
            }
        }

        log.warn("No sound to play. Check if the directory is well created/filled and the app configuration");
        return Optional.empty();
    }

    public Optional<String> getRandomSound(String name) {
        return Stream.of(getSounds())
                .filter(sound -> sound.getName().toLowerCase().startsWith(name))
                .findAny()
                .map(file -> {
                    try {
                        return file.getCanonicalPath();
                    } catch (IOException e) {
                        log.error(e.getMessage());
                    }
                    return null;
                });
    }

    private File[] getSounds(){
        File file = new File(soundsLocation);
        return Optional.ofNullable(file.listFiles()).orElse(EMPTY_SOUNDS);
    }

    public List<String> getSoundList(){
        return Stream.of(getSounds())
                .map(file -> file.getName().toLowerCase().replaceFirst("[.][^.]+$", StringUtils.EMPTY))
                .collect(Collectors.toList());
    }
}
