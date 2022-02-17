package de.miku.lina.commands.information;

import de.miku.lina.commands.Command;
import de.miku.lina.spring.entity.DBUser;
import de.miku.lina.utils.DataShare;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class cmdDescription extends Command {
    public cmdDescription() {
        super("description", "set you're own description");
        commandActive = true;
        messageCompatible = true;
    }

    @Override
    protected void generateCommandData() {

    }

    @Override
    public void onSlash(SlashCommandInteractionEvent event) {

    }

    @Override
    public void onMessage(MessageReceivedEvent event, String[] args) {
        DBUser user = DataShare.userHandler.getUser(event.getAuthor());
        if (args.length < 1) {
            event.getMessage().reply(user.getDescription()).queue();
            return;
        }
        user.setDescription(String.join(" ", args));
        event.getMessage().reply("i set you're personal description").queue();
    }
}
