package de.miku.lina.listeners;

import de.miku.lina.utils.DataShare;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class SlashListener extends ListenerAdapter {


    @Override
    public void onSlashCommand(@NotNull SlashCommandEvent event) {
        DataShare.commandHandler.onSlashCommand(event);
    }
}
