package de.miku.lina.utils;

import de.miku.lina.commands.Command;
import de.miku.lina.handlers.CommandHandler;
import de.miku.lina.handlers.ConfigHandler;
import de.miku.lina.handlers.GuildHandler;
import de.miku.lina.handlers.InteractionHandler;
import net.dv8tion.jda.api.JDA;

import java.util.HashMap;
import java.util.Map;

public class DataShare {

    public static String prefix;

    public static ConfigHandler configHandler;

    public static CommandHandler commandHandler;
    public static InteractionHandler interactionHandler;
    public static GuildHandler guildHandler;

    public static JDA jda;

    public static Map<String, Command> commands = new HashMap<String, Command>();



}
