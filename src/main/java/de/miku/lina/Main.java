package de.miku.lina;

import de.miku.lina.commands.fun.cmdHey;
import de.miku.lina.commands.information.cmdDescription;
import de.miku.lina.commands.information.cmdHelp;
import de.miku.lina.commands.information.cmdHelpFull;
import de.miku.lina.commands.interactions.cmdInteracts;
import de.miku.lina.commands.moderation.*;
import de.miku.lina.handlers.*;
import de.miku.lina.listeners.MessageListener;
import de.miku.lina.listeners.ReactionListener;
import de.miku.lina.listeners.SlashListener;
import de.miku.lina.spring.SpringRepos;
import de.miku.lina.spring.entity.DBUser;
import de.miku.lina.spring.repo.DBUserRepository;
import de.miku.lina.utils.DataShare;
import de.miku.lina.utils.Logging;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.internal.utils.AllowedMentionsImpl;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.security.auth.login.LoginException;
import javax.transaction.Transactional;
import java.io.IOException;
import java.time.LocalDateTime;

@SpringBootApplication
@Transactional
public class Main implements CommandLineRunner {


    public static void main(String[] args) {
        // Start spring
        SpringApplication.run(Main.class, args);
    }


    public Main(DBUserRepository dbUserRepository) {
        SpringRepos.dbUserRepository = dbUserRepository;
        try {
            startBot();
        } catch (LoginException e) {
            Logging.error("CAN'T START DISCORD BOT");
            System.exit(0);
        }
    }

    public static void startBot() throws LoginException {
        // "enable single core support for jda"
        final int cores = Runtime.getRuntime().availableProcessors();
        if (cores <= 1) {
            System.out.println("Available Cores \"" + cores + "\", setting Parallelism Flag");
            System.setProperty("java.util.concurrent.ForkJoinPool.common.parallelism", "1");
        }
        // init config handler
        try {
            DataShare.configHandler = new ConfigHandler("./config.ini");
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(0);
        }
        // Disable all mentions on replay
        AllowedMentionsImpl.setDefaultMentionRepliedUser(false);
        // construct Builder with token
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
        // load the guilds
        DataShare.guildHandler = new GuildHandler();
        // register user handler
        DataShare.userHandler = new UserHandler(SpringRepos.dbUserRepository);

        // build the bot
        DataShare.jda = builder.build();
        // init the command handler
        DataShare.commandHandler = new CommandHandler(DataShare.configHandler.getString("DISCORD", "prefix"));
        // register commands
        registerCommands();
        // "re"load command system
        DataShare.commandHandler.loadCommands();
        // init the interaction handler after the command handler, for not floating the help list
        DataShare.interactionHandler = new InteractionHandler();

        // init handlers after build the jda
        Logging.info(Main.class, "Bot is online...");


    }

    private static void registerEvent(JDABuilder builder) {
        builder.addEventListeners(new MessageListener(), new SlashListener(), DataShare.eventWaiter, new ReactionListener());
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
        new cmdEmbedCreator();
        new cmdDescription();
        // init commands that should be hidden in the help list
        DataShare.commandHandler.setHiddenCommand(new cmdShutdown());
    }


    @Override
    public void run(String... args) throws Exception {

    }
}
