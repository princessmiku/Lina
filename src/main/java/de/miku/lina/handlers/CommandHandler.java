package de.miku.lina.handlers;

import de.miku.lina.commands.Command;
import de.miku.lina.commands.interactions.InteractionCommand;
import de.miku.lina.utils.DataShare;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.requests.restaction.CommandListUpdateAction;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class CommandHandler {

    private Map<String, Command> slashCommands;
    private Map<String, Command> textCommands;
    private Map<String, InteractionCommand> sfwCommands, nsfwCommands;
    private String prefix;
    private MessageEmbed helpEmbed;
    private boolean finish;

    public CommandHandler() {
        finish = false;
        prefix = DataShare.configHandler.getString("DISCORD", "prefix");
        slashCommands = new HashMap<>();
        textCommands = new HashMap<>();
        sfwCommands = new HashMap<>();
        nsfwCommands = new HashMap<>();

        CommandListUpdateAction commandListUpdateAction = DataShare.jda.updateCommands();

        // for each command check is possible
        for (Map.Entry<String, Command> entry : DataShare.commands.entrySet()) {
            if (!entry.getValue().isActive()) continue;
            if (entry.getValue().isSlashCompatible()){
                commandListUpdateAction.addCommands(entry.getValue().getCommandData());
                slashCommands.put(entry.getKey().toLowerCase(), entry.getValue());
            }
            if (entry.getValue().isMessageCompatible()) {
                textCommands.put(entry.getKey().toLowerCase(), entry.getValue());
            }

        }

        // submit slash commands
        commandListUpdateAction.queue();
        generateHelpEmbed();
        finish = true;
    }

    public void addInteractCommands(Map<String, InteractionCommand> sfw, Map<String, InteractionCommand> nsfw) {
        sfwCommands.putAll(sfw);
        nsfwCommands.putAll(nsfw);
    }

    public void generateHelpEmbed() {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle(":notepad_spiral: Help from " + DataShare.jda.getSelfUser().getName());
        String desc = "Active Commands\nSlash Commands market with :small_orange_diamond:\nText Command market with :small_blue_diamond:\n\n";
        Map<String, ArrayList<String>> category = new HashMap<String, ArrayList<String>>();
        for (Map.Entry<String, Command> entry : DataShare.commands.entrySet()) {
            if (!entry.getValue().isActive()) continue;
            String myde = "";
            if (entry.getValue().isSlashCompatible())
                myde += ":small_orange_diamond:";
            else
                myde += ":black_small_square:";

            if (entry.getValue().isMessageCompatible())
                myde += ":small_blue_diamond:";
            else
                myde += ":black_small_square:";
            String categoryName = entry.getValue().getCategory().substring(0, 1).toUpperCase() + entry.getValue().getCategory().substring(1);
            if (!category.containsKey(categoryName)) category.put(categoryName, new ArrayList<String>());
            category.get(categoryName).add(myde + " `%s`\n".formatted(entry.getKey().toLowerCase()));
        }
        ArrayList<String> sortedKeys = new ArrayList(category.keySet());
        Collections.sort(sortedKeys);

        for (String key : sortedKeys) {
            desc += "\n**%s**\n".formatted(key);
            for (String de: category.get(key)) {
                desc += de;
            }
        }

        embedBuilder.setDescription(desc);
        embedBuilder.setFooter("You want to see all commands? Use %shelpfull".formatted(prefix));
        helpEmbed = embedBuilder.build();
    }

    public void onSlashCommand(@NotNull SlashCommandEvent event) {
        // get name of the event
        String name = event.getName();
        // get command
        Command command = slashCommands.get(name);
        // check if command exits and run it
        if (command != null) command.onSlash(event);
    }

    public void onTextCommand(@NotNull MessageReceivedEvent event) {
        // check if message starts with prefix and the author is not a bot
        if (!event.getMessage().getContentDisplay().startsWith(prefix) || event.getAuthor().isBot()) return;
        if (!event.getGuild().getSelfMember().hasPermission(event.getTextChannel(), Permission.MESSAGE_WRITE) || !event.getGuild().getSelfMember().hasPermission(event.getTextChannel(), Permission.MESSAGE_EMBED_LINKS))
            return;
        // split the message in args
        String[] args = event.getMessage().getContentRaw().split(" ");
        // get invoke
        String invoke = args[0].substring(prefix.length());
        // get command
        Command command = textCommands.get(invoke);
        // check if command exists and run it
        if (command != null) command.onMessage(event, Arrays.copyOfRange(args, 1, args.length));
        else {
            if (sfwCommands.containsKey(invoke) || nsfwCommands.containsKey(invoke)) {
                InteractionCommand sfw = sfwCommands.get(invoke);
                InteractionCommand nsfw = nsfwCommands.get(invoke);
                if (event.getTextChannel().isNSFW() && nsfw != null) {
                    nsfw.onMessage(event);
                } else if (sfw != null) {
                    sfw.onMessage(event);
                }
            }
        }
    }

    public void addTextCommand(String name, Command command) {
        textCommands.put(name, command);
    }

    public boolean isFinish() {
        return finish;
    }

    public MessageEmbed getHelpEmbed() {
        return helpEmbed;
    }

}
