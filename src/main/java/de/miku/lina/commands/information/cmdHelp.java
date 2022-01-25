package de.miku.lina.commands.information;

import de.miku.lina.commands.Command;
import de.miku.lina.utils.DataShare;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

public class cmdHelp extends Command {
    public cmdHelp() {
        super("help", "list all commands");
        commandActive = true;
        slashCompatible = true;
        messageCompatible = true;
    }

    @Override
    protected void generateCommandData() {
        commandData = new CommandData(name, description);
    }

    @Override
    public void onSlash(SlashCommandEvent event) {
        event.replyEmbeds(DataShare.commandHandler.getHelpEmbed()).queue();
    }

    @Override
    public void onMessage(MessageReceivedEvent event, String[] args) {
        event.getMessage().reply(DataShare.commandHandler.getHelpEmbed()).queue();
    }

}
