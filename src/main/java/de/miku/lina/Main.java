package de.miku.lina;

import de.miku.lina.commands.fun.cmdHey;
import de.miku.lina.commands.information.cmdHelp;
import de.miku.lina.commands.information.cmdHelpFull;
import de.miku.lina.commands.interactions.InteractionCommand;
import de.miku.lina.commands.interactions.cmdInteracts;
import de.miku.lina.commands.moderation.cmdAddReactionRole;
import de.miku.lina.commands.moderation.cmdBan;
import de.miku.lina.commands.moderation.cmdClear;
import de.miku.lina.commands.moderation.cmdShutdown;
import de.miku.lina.handlers.CommandHandler;
import de.miku.lina.handlers.ConfigHandler;
import de.miku.lina.handlers.GuildHandler;
import de.miku.lina.handlers.InteractionHandler;
import de.miku.lina.listeners.MessageListener;
import de.miku.lina.listeners.SlashListener;
import de.miku.lina.utils.DataShare;
import de.miku.lina.utils.Logging;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.internal.utils.AllowedMentionsImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.security.auth.login.LoginException;
import java.io.FileNotFoundException;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws LoginException {
        // init config handler
        try {
            DataShare.configHandler = new ConfigHandler("./src/main/resources/config.ini");
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(0);
        }
        // construct Builder with token
        AllowedMentionsImpl.setDefaultMentionRepliedUser(false);
        JDABuilder builder = JDABuilder.createDefault(DataShare.configHandler.getString("DISCORD", "token"));
        // set the bot settings
        // set the activity
        builder.setActivity(Activity.playing(DataShare.configHandler.getString("DISCORD", "status")));
        // set the online status
        builder.setStatus(OnlineStatus.DO_NOT_DISTURB);
        // set intents
        builder.enableIntents(
                GatewayIntent.GUILD_MESSAGES,
                GatewayIntent.GUILD_MEMBERS,
                GatewayIntent.GUILD_MESSAGE_REACTIONS,
                GatewayIntent.GUILD_BANS,
                GatewayIntent.GUILD_PRESENCES
        );
        // register events
        registerEvent(builder);
        registerCommands();
        // load the guilds
        DataShare.guildHandler = new GuildHandler();

        // build the bot
        DataShare.jda = builder.build();
        // init the command handler
        DataShare.commandHandler = new CommandHandler();
        // init the interaction handler after the command handler, for not floating the help list
        DataShare.interactionHandler = new InteractionHandler();
        // init commands that should be hidden, hidden commands are not slash compatible
        registerHiddenCommands();

        // init handlers after build the jda
        Logging.info(Main.class, "Bot is online...");


    }

    private static void registerEvent(JDABuilder builder) {
        builder.addEventListeners(new MessageListener(), new SlashListener(), DataShare.eventWaiter);
    }

    public static void registerCommands() {
        // setup prefix global
        DataShare.prefix = DataShare.configHandler.getString("DISCORD", "prefix");

        // setup commands
        new cmdHelp();
        new cmdHey();
        new cmdBan();
        new cmdClear();
        new cmdInteracts();
        new cmdAddReactionRole();
        new cmdHelpFull();
    }

    public static void registerHiddenCommands() {
        new cmdShutdown();
    }

}
