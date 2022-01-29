package de.miku.lina.commands.interactions;

import de.miku.lina.commands.Command;
import de.miku.lina.handlers.InteractionHandler;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

public class cmdInteracts extends Command {
    public cmdInteracts() {
        super("interacts", "show all possible interactions, for NSFW use this command in a nsfw channel");
        messageCompatible = true;
        commandActive = true;
        slashCompatible = true;
    }

    @Override
    protected void generateCommandData() {
        commandData = new CommandData(name, description);
    }

    @Override
    public void onSlash(SlashCommandEvent event) {
        if (event.getTextChannel().isNSFW()) {
            event.replyEmbeds(InteractionHandler.nsfwEmbed).queue();
        } else {
            event.replyEmbeds(InteractionHandler.sfwEmbed).queue();
        }

    }

    @Override
    public void onMessage(MessageReceivedEvent event, String[] args) {
        if (event.getTextChannel().isNSFW()) {
            event.getMessage().reply(InteractionHandler.nsfwEmbed).queue();
        } else {
            event.getMessage().reply(InteractionHandler.sfwEmbed).queue();
        }
    }
}
