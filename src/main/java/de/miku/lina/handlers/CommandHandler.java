package de.miku.lina.handlers;

import de.miku.lina.commands.Command;
import de.miku.lina.commands.interactions.InteractionCommand;
import de.miku.lina.utils.DataShare;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.requests.restaction.CommandListUpdateAction;
import org.jetbrains.annotations.NotNull;

import javax.xml.crypto.Data;
import java.util.*;

public class CommandHandler {


    // list of commands
    private Map<String, Command> rawCommands = new HashMap<>();
    private Map<String, Command> slashCommands = new HashMap<>();
    private Map<String, Command> messageCommands = new HashMap<>();
    private List<String> hiddenCommands = new ArrayList<>();
    // list of interaction commands, hidden in help list, but can't register on normal way
    private Map<String, InteractionCommand> sfwCommands = new HashMap<>(), nsfwCommands = new HashMap<>();

    // Prefix of the commands
    private String prefix;
    // An embed for the help
    private MessageEmbed helpEmbed = new EmbedBuilder().setDescription("*nicht vorhanden, der Bot startet noch*").build();
    // is commands finish loading?
    private boolean finish = false;

    public CommandHandler(String prefix) {
        this.prefix = prefix;
    }

    public void loadCommands() {
        // reset all commands and set it in loading
        finish = false;
        slashCommands.clear();
        messageCommands.clear();

        // get the bots slash command list for entry own commands
        CommandListUpdateAction commandListUpdateAction = DataShare.jda.updateCommands();

        // for each command check is possible to use and witch it is
        for (Map.Entry<String, Command> entry : rawCommands.entrySet()) {
            // check if command active, on inactive continue
            if (!entry.getValue().isActive()) continue;
            // check if command available as slash command
            if (entry.getValue().isSlashCompatible()){
                // add slash command data to the list
                CommandListUpdateAction __ = commandListUpdateAction.addCommands(entry.getValue().getCommandData());
                // put the slash command in the slash command map
                slashCommands.put(entry.getKey().toLowerCase(), entry.getValue());
            }
            // check if command available as message command
            if (entry.getValue().isMessageCompatible()) {
                // put the message command in the message command map
                messageCommands.put(entry.getKey().toLowerCase(), entry.getValue());
            }
        }
        // end of for

        // submit the slash command to discord
        // this process can take some time on the part
        // of discord until the commands are all displayed correctly
        commandListUpdateAction.complete();

        // update the help embed with the new commands
        generateHelpEmbed();

        // set lodaing is finish
        finish = true;
    }

    public void generateHelpEmbed() {
        // create a new embed builder
        EmbedBuilder embedBuilder = new EmbedBuilder();
        // set the title
        embedBuilder.setTitle(":notepad_spiral: Help from " + DataShare.jda.getSelfUser().getName());
        // set first part of the description
        StringBuilder desc = new StringBuilder("My prefix is `%s`\n\n**Active Commands**\nSlash Commands market with :small_orange_diamond:\nText Command market with :small_blue_diamond:\n\n".formatted(prefix));
        // new hashmap for commands with their category
        Map<String, ArrayList<String>> category = new HashMap<>();
        // I think it is a for loop for the entry's in raw commands, but I don't know
        for (Map.Entry<String, Command> entry : rawCommands.entrySet()) {
            // check if command is active, double check is better xD
            // is not, continue
            if (!entry.getValue().isActive()) continue;
            String myde = "";
            // check if command slash compatible, is it show it in the help
            if (entry.getValue().isSlashCompatible())
                myde += ":small_orange_diamond:";
                // or make it black
            else
                myde += ":black_small_square:";
            // check if command message compatible, is it show it in the help
            if (entry.getValue().isMessageCompatible())
                myde += ":small_blue_diamond:";
                // or make it black
            else
                myde += ":black_small_square:";

            // I don't know what it does, but it works
            String categoryName = entry.getValue().getCategory().substring(0, 1).toUpperCase() + entry.getValue().getCategory().substring(1);
            if (!category.containsKey(categoryName)) category.put(categoryName, new ArrayList<>());
            category.get(categoryName).add(myde + " `%s`\n".formatted(entry.getKey().toLowerCase()));
        }
        // create an array list with the keys
        ArrayList<String> sortedKeys = new ArrayList(category.keySet());
        // sort the array
        Collections.sort(sortedKeys);

        // add all content from the array to the description
        // TODO: Do it in fields, not in single descriptions
        for (String key : sortedKeys) {
            desc.append("\n**%s**\n".formatted(key));
            for (String de: category.get(key)) {
                desc.append(de);
            }
        }
        // set the description
        embedBuilder.setDescription(desc.toString());

        // build the embed and set is as current
        helpEmbed = embedBuilder.build();
    }

    public void onSlashCommand(@NotNull SlashCommandInteractionEvent event) {
        // check if can bot send messages and embeds, else cancel the event
        if (!event.getGuild().getSelfMember().hasPermission(event.getTextChannel(), Permission.MESSAGE_SEND) || !event.getGuild().getSelfMember().hasPermission(event.getTextChannel(), Permission.MESSAGE_EMBED_LINKS)) {
            // reply a deaf answer
            event.deferReply().queue();
            return;
        }
        // get name of the event
        String name = event.getName();
        // get command
        Command command = slashCommands.get(name);
        // check if command exits and run it
        if (command != null) {
            if (!command.canInteract(event)) return;
            command.onSlash(event);
        }
    }

    public void onTextCommand(@NotNull MessageReceivedEvent event) {
        // check if guild registered
        DataShare.guildHandler.checkGuild(event.getGuild());

        // check if message starts with prefix and the author is not a bot
        if (!event.getMessage().getContentDisplay().startsWith(prefix) || event.getAuthor().isBot()) return;
        if (!event.getGuild().getSelfMember().hasPermission(event.getTextChannel(), Permission.MESSAGE_SEND) || !event.getGuild().getSelfMember().hasPermission(event.getTextChannel(), Permission.MESSAGE_EMBED_LINKS))
            return;
        // split the message in args
        String[] args = event.getMessage().getContentRaw().split(" ");
        // get invoke
        String invoke = args[0].substring(prefix.length());
        // get command
        Command command = messageCommands.get(invoke);
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

    public void setHiddenCommand(Command command) {
        if (!hiddenCommands.contains(command.getName())) hiddenCommands.add(command.getName());
    }

    // add commands
    public void addCommand(Command command) {
        rawCommands.put(command.getName(), command);
    }

    // remove commands
    public void removeCommand(Command command) {
        if (rawCommands.containsKey(command.getName())) rawCommands.remove(command.getName());
    }

    // update a command, it's only the new command, but not change the name
    public void updateCommand(Command command) {
        addCommand(command);
    }

    public void addInteractCommands(Map<String, InteractionCommand> sfw, Map<String, InteractionCommand> nsfw) {
        sfwCommands.putAll(sfw);
        nsfwCommands.putAll(nsfw);
    }

    public boolean isFinish() {
        return finish;
    }

    public MessageEmbed getHelpEmbed() {
        return helpEmbed;
    }

}
