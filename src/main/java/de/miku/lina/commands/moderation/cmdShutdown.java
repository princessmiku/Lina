package de.miku.lina.commands.moderation;

import de.miku.lina.commands.Command;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class cmdShutdown extends Command {
    public cmdShutdown() {
        super("shutdown", "shut the bot up");
        commandActive = true;
        messageCompatible = true;
    }

    @Override
    protected void generateCommandData() {

    }

    @Override
    public void onSlash(SlashCommandEvent event) {

    }

    @Override
    public void onMessage(MessageReceivedEvent event, String[] args) {
        if (!event.getAuthor().getId().equals("293135882926555137")) {
            event.getMessage().reply("no").queue();
            return;
        }
        event.getMessage().reply(":wave:").queue(e -> {
            System.exit(0);
        });
    }
}
