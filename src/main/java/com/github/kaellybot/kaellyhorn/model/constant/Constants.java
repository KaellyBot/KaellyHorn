package com.github.kaellybot.kaellyhorn.model.constant;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.track.playback.NonAllocatingAudioFrameBuffer;

public final class Constants {

    private Constants(){}

    public static final String DEFAULT_PREFIX = "!";

    public static final Language DEFAULT_LANGUAGE = Language.FR;

    public static final String NAME = "KaellyHorn";

    public static final String VERSION = "1.0.0";

    public static final int PREFIX_MAXIMUM_LENGTH = 3;

    public static final AudioPlayerManager PLAYER_MANAGER;

    static {
        PLAYER_MANAGER = new DefaultAudioPlayerManager();
        PLAYER_MANAGER.getConfiguration().setFrameBufferFactory(NonAllocatingAudioFrameBuffer::new);
        AudioSourceManagers.registerRemoteSources(PLAYER_MANAGER);
        AudioSourceManagers.registerLocalSource(PLAYER_MANAGER);
    }
}