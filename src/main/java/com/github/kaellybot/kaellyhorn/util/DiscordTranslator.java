package com.github.kaellybot.kaellyhorn.util;

import com.github.kaellybot.commons.model.constants.Language;
import com.github.kaellybot.commons.util.Translator;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import static com.github.kaellybot.kaellyhorn.model.constant.Constants.*;

@Component
@Log
public class DiscordTranslator extends Translator {

    private Language language;

    private final String prefix;

    public DiscordTranslator(@Value("${language}") String language, @Value("${prefix}") String prefix){
        try {
            this.language = Language.valueOf(language.toUpperCase());
        } catch(IllegalArgumentException e){
            this.language = DEFAULT_LANGUAGE;
            log.warning("No language provided. Default language used: " + DEFAULT_LANGUAGE);
        }

        if (prefix != null && prefix.length() <= PREFIX_MAXIMUM_LENGTH)
            this.prefix = prefix;
        else {
            log.warning("Prefix is not provided or does not respect the constraint of 3 maximum characters. Default prefix used: " + DEFAULT_PREFIX);
            this.prefix = DEFAULT_PREFIX;
        }

    }

    public Language getLanguage(){
        return this.language;
    }

    public String getPrefix(){
        return this.prefix;
    }
}