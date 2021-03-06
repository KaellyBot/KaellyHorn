package com.github.kaellybot.kaellyhorn;

import com.github.kaellybot.kaellyhorn.service.DiscordService;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages="com.github.kaellybot")
public class CoreApplication  implements CommandLineRunner {

    private final DiscordService discordService;

    public CoreApplication(DiscordService discordService){
        this.discordService = discordService;
    }

    @Override
    public void run(String... args) {
        discordService.startBot();
    }

	public static void main(String[] args) {
		SpringApplication.run(CoreApplication.class, ArrayUtils.EMPTY_STRING_ARRAY);
	}
}