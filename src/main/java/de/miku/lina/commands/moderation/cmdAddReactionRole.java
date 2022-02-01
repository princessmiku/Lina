package de.miku.lina.commands.moderation;

import de.miku.lina.commands.Command;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

public class cmdAddReactionRole extends Command {
    public cmdAddReactionRole() {
        super("add reaction role", "add a role that given, when someone interact with a reaction");
        commandActive = true;
        messageCompatible = true;
        slashCompatible = true;
    }

    @Override
    protected void generateCommandData() {
        commandData = new CommandData(name, description);
    }

    @Override
    public void onSlash(SlashCommandEvent event) {
        event.reply("In work... use message command").queue();
    }

    @Override
    public void onMessage(MessageReceivedEvent event, String[] args) {

    }
}
