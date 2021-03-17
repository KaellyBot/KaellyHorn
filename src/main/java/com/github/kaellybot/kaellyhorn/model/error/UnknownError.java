package com.github.kaellybot.kaellyhorn.model.error;

import com.github.kaellybot.kaellyhorn.model.constant.Error;
import com.github.kaellybot.kaellyhorn.model.constant.Language;
import com.github.kaellybot.kaellyhorn.util.DiscordTranslator;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class UnknownError implements Error {

    @Override
    public String getLabel(DiscordTranslator translator, Language language){
        return translator.getLabel(language, "error.unknown");
    }
}
