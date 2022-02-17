package de.miku.lina.commands.fun;

import de.miku.lina.commands.Command;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class cmdHey extends Command {
    public cmdHey() {
        super("hey", "say hey to me");
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
        event.getMessage().reply("hiiiiii").queue();
    }

}
