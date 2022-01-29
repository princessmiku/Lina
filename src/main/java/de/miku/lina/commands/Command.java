package de.miku.lina.commands;

import de.miku.lina.utils.DataShare;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

public abstract class Command {

    protected String name, description, category;
    protected CommandData commandData;
    protected Boolean slashCompatible = false, messageCompatible = false, commandActive = false;

    public Command(String name, String description) {
        this.name = name;
        this.description = description;
        String[] theName = this.getClass().getPackageName().split("\\.");
        this.category = theName[theName.length - 1];
        DataShare.commands.put(this.name, this);
        if (DataShare.commandHandler != null) {
            if (DataShare.commandHandler.isFinish()) {
                DataShare.commandHandler.addTextCommand(this.name, this);
            }
        }
    }

    protected abstract void generateCommandData();

    public abstract void onSlash(SlashCommandEvent event);

    public abstract void onMessage(MessageReceivedEvent event, String[] args);

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public String getDescription() {
        return description;
    }

    public CommandData getCommandData() {
        if (commandData == null) generateCommandData();
        return commandData;
    }

    public Boolean isSlashCompatible() {
        return slashCompatible;
    }

    public Boolean isMessageCompatible() {
        return messageCompatible;
    }

    public Boolean isActive() {
        return commandActive;
    }

}
