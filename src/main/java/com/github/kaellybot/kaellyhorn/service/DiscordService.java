package com.github.kaellybot.kaellyhorn.service;

import com.github.kaellybot.kaellyhorn.command.sound.SoundCommand;
import com.github.kaellybot.kaellyhorn.command.util.Command;
import com.github.kaellybot.kaellyhorn.util.DiscordTranslator;
import discord4j.core.DiscordClient;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.lifecycle.ReconnectEvent;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.presence.Activity;
import discord4j.core.object.presence.Presence;
import discord4j.core.shard.MemberRequestFilter;
import discord4j.gateway.intent.Intent;
import discord4j.gateway.intent.IntentSet;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class DiscordService {

    private DiscordClient discordClient;

    @Value("${discord.token}")
    private String token;

    private final List<Command> commands;

    private final DiscordTranslator translator;

    private String presence;

    public DiscordService(List<Command> commands,  DiscordTranslator translator){
        this.commands = commands;
        this.translator = translator;

        presence = this.translator.getPrefix() + SoundCommand.COMMAND_NAME + " help";
    }

    public void startBot(){
        if (discordClient == null){
            discordClient = DiscordClient.create(token);
            discordClient.gateway()
                    .setEnabledIntents(IntentSet.of(
                            Intent.GUILDS,
                            Intent.GUILD_MESSAGES,
                            Intent.DIRECT_MESSAGES))
                    .setInitialStatus(ignored -> Presence.online(Activity.watching(presence)))
                    .setMemberRequestFilter(MemberRequestFilter.none())
                    .withGateway(client -> Mono.when(
                        reconnectListener(client),
                        commandListener(client)))
                    .subscribe();
        }
    }

    private Mono<Void> commandListener(GatewayDiscordClient client){
        return client.getEventDispatcher().on(MessageCreateEvent.class)
                .map(MessageCreateEvent::getMessage)
                .filterWhen(message -> message.getAuthorAsMember().map(member -> ! member.isBot()))
                .flatMap(msg -> Flux.fromIterable(commands)
                        .flatMap(cmd -> cmd.request(msg, translator.getPrefix(), translator.getLanguage())))
                .then();
    }

    private Mono<Void> reconnectListener(GatewayDiscordClient client){
        return client.getEventDispatcher().on(ReconnectEvent.class)
                .flatMap(event -> event.getClient()
                        .updatePresence(Presence.online(Activity.watching(presence))))
                .then();
    }
}