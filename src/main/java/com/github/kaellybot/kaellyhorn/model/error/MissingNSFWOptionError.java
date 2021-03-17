package com.github.kaellybot.kaellyhorn.model.error;

import com.github.kaellybot.kaellyhorn.model.constant.Error;
import com.github.kaellybot.kaellyhorn.model.constant.Language;
import com.github.kaellybot.kaellyhorn.util.DiscordTranslator;

public class MissingNSFWOptionError implements Error {

    @Override
    public String getLabel(DiscordTranslator translator, Language language) {
        return translator.getLabel(language, "error.missing_nsfw");
    }
}
