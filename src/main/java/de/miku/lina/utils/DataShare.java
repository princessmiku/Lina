package de.miku.lina.utils;

import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import de.miku.lina.commands.Command;
import de.miku.lina.handlers.*;
import net.dv8tion.jda.api.JDA;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataShare {

    public static String prefix;
    public static ConfigHandler configHandler;

    public static CommandHandler commandHandler;
    public static InteractionHandler interactionHandler;
    public static GuildHandler guildHandler;
    public static UserHandler userHandler;

    public static JDA jda;

    public static List<String> ignoreReactions = new ArrayList<>();
    public static Map<String, Command> commands = new HashMap<>();
    public static EventWaiter eventWaiter = new EventWaiter();



}
