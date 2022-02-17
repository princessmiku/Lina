package de.miku.lina.commands.information;

import de.miku.lina.commands.Command;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.internal.interactions.CommandDataImpl;

public class cmdHelpFull extends Command {

    public cmdHelpFull() {
        super("helpfull", "give the complete command list");
        commandActive = true;
        messageCompatible = true;
        slashCompatible = true;
    }

    @Override
    protected void generateCommandData() {
        commandData = new CommandDataImpl(name, description);
    }

    @Override
    public void onSlash(SlashCommandInteractionEvent event) {
        event.reply("*not usable currently*").queue();
    }

    @Override
    public void onMessage(MessageReceivedEvent event, String[] args) {
        event.getMessage().reply("*not usable currently*").queue();
    }
}
