package de.miku.lina.commands.information;

import de.miku.lina.commands.Command;
import de.miku.lina.utils.DataShare;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.internal.interactions.CommandDataImpl;

public class cmdHelp extends Command {
    public cmdHelp() {
        super("help", "list all commands");
        commandActive = true;
        slashCompatible = true;
        messageCompatible = true;
    }

    @Override
    protected void generateCommandData() {
        commandData = new CommandDataImpl(name, description);
    }

    @Override
    public void onSlash(SlashCommandInteractionEvent event) {
        event.replyEmbeds(DataShare.commandHandler.getHelpEmbed()).queue();
    }

    @Override
    public void onMessage(MessageReceivedEvent event, String[] args) {
        event.getMessage().replyEmbeds(DataShare.commandHandler.getHelpEmbed()).queue();
    }

}
