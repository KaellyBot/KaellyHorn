package com.github.kaellybot.kaellyhorn.util;

import com.github.kaellybot.kaellyhorn.model.constant.Error;
import com.github.kaellybot.kaellyhorn.model.constant.Language;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

import static com.github.kaellybot.kaellyhorn.model.constant.Constants.*;

@Slf4j
@Component
public class DiscordTranslator {

    private Language language;
    private final String prefix;
    private final Map<Language, Properties> labels;
    protected final Random random;

    public DiscordTranslator(@Value("${language}") String language, @Value("${prefix}") String prefix){
        try {
            this.language = Language.valueOf(language.toUpperCase());
        } catch(IllegalArgumentException e){
            this.language = DEFAULT_LANGUAGE;
            log.warn("No language provided. Default language used: {}", DEFAULT_LANGUAGE);
        }

        if (prefix != null && prefix.length() <= PREFIX_MAXIMUM_LENGTH)
            this.prefix = prefix;
        else {
            log.warn("Prefix is not provided or does not respect the constraint of 3 maximum characters. Default prefix used: {}", DEFAULT_PREFIX);
            this.prefix = DEFAULT_PREFIX;
        }

        labels = new ConcurrentHashMap<>();
        random = new Random();
        Stream.of(Language.values()).forEach(this::register);

    }

    protected void register(Language language){
        try(InputStream file = getClass().getResourceAsStream("/" + language.getFileName())) {
            loadLabels(language, file);
        } catch (NullPointerException e) {
            log.error("{} cannot be registered because '{}' does not exist", language, language.getFileName());
        } catch (Exception e) {
            log.error("{} cannot be registered:", language, e);
        }
    }

    protected void loadLabels(Language language, InputStream file) throws IOException {
        Properties prop = new Properties();
        prop.load(new BufferedReader(new InputStreamReader(file, StandardCharsets.UTF_8)));
        labels.put(language, prop);
    }

    protected String getInternalLabel(Language lang, String key){
        if (! labels.containsKey(lang)) {
            log.warn("The language {} is not registered", lang);
            return key;
        }

        return Optional.ofNullable(labels.get(lang))
                .map(property -> property.getProperty(key, key))
                .filter(label -> ! label.trim().isEmpty())
                .orElseGet(() -> {
                    log.warn("Missing label in {} for property '{}'", lang, key);
                    return key;
                });
    }

    public String getLabel(Language lang, String property, Object... arguments){
        String value = getInternalLabel(lang, property);

        for(Object arg : arguments)
            value = value.replaceFirst("\\{}", String.valueOf(arg));

        return value;
    }

    public String getLabel(Language lang, Error error){
        return error.getLabel(this, lang);
    }

    public String getRandomLabel(Language lang, String property, Object... arguments){
        String[] values = getInternalLabel(lang, property).split(";");
        String value = values[random.nextInt(values.length)];

        for(Object arg : arguments)
            value = value.replaceFirst("\\{}", String.valueOf(arg));

        return value;
    }

    public Language getLanguage(){
        return this.language;
    }

    public String getPrefix(){
        return this.prefix;
    }
}