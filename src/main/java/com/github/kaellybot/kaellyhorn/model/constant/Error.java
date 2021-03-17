package com.github.kaellybot.kaellyhorn.model.constant;


import com.github.kaellybot.kaellyhorn.util.DiscordTranslator;

public interface Error {

    String getLabel(DiscordTranslator translator, Language language);
}
