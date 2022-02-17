package de.miku.lina.commands;

import de.miku.lina.utils.DataShare;
import de.miku.lina.utils.PermChecker;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

import java.util.ArrayList;
import java.util.List;

public abstract class Command {

    // Name, description and category
    protected String name, description, category;
    protected CommandData commandData;
    protected Boolean slashCompatible = false, messageCompatible = false, commandActive = false;
    protected List<Permission> needPermission = new ArrayList<>();

    public Command(String name, String description) {
        // to lower case to termite errors
        this.name = name.toLowerCase();
        this.description = description;
        // get the name of the package where the command is in
        String[] theName = this.getClass().getPackageName().split("\\.");
        // set is as command category
        this.category = theName[theName.length - 1];
        // register the command in the command system
        if (DataShare.commandHandler != null) {
            DataShare.commandHandler.addCommand(this);
        }
    }

    protected abstract void generateCommandData();

    public abstract void onSlash(SlashCommandInteractionEvent event);

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

    public void addNeedPermission(Permission permission) {
        if (!needPermission.contains(permission)) needPermission.add(permission);
    }

    public List<Permission> getNeedPermission() {
        return needPermission;
    }

    public boolean canInteract(SlashCommandInteractionEvent event) {
        return PermChecker.handlePermissionCheck(event, needPermission);
    }
}
