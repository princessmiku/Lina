package de.miku.lina.utils;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;

public class DiscordEmbeds {

    public static MessageEmbed error(User user, String inpu) {
        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle(":no_entry_sign: Error");
        embed.setColor(ColorPlate.RED);
        embed.setDescription(inpu);
        return embed.build();
    }

    public static MessageEmbed internalError() {
        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle(":no_entry_sign: Internal error");
        embed.setColor(ColorPlate.RED);
        embed.setDescription("Something went wrong, but I do not know what.");
        return embed.build();
    }

    public static MessageEmbed invalidSelfPermission(Permission permission) {
        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle(":no_entry_sign: Invalid Permission for me");
        embed.setColor(ColorPlate.RED);
        embed.setDescription("I need the follow permission `%s` to use this command there".formatted(permission.getName()));
        return embed.build();
    }


    public static MessageEmbed noMention(User user) {
        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle(":no_entry_sign: Invalid Input");
        embed.setColor(ColorPlate.RED);
        embed.setDescription("No Member is mentioned");
        return embed.build();
    }


    public static MessageEmbed noInput(User user, String inpu) {
        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle(":no_entry_sign: Invalid Input");
        embed.setColor(ColorPlate.RED);
        embed.setDescription("You have forgotten the following input `%s`".formatted(inpu));
        return embed.build();
    }


    public static MessageEmbed invalidInput(User user, String inpu) {
        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle(":no_entry_sign: Invalid Input");
        embed.setColor(ColorPlate.RED);
        embed.setDescription("Use `%s`".formatted(inpu));
        return embed.build();
    }

    public static MessageEmbed success(User user, String message) {
        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle(":white_check_mark: Success `%s`".formatted(message));
        embed.setColor(ColorPlate.GREEN);
        return embed.build();
    }

    public static MessageEmbed invalidPermission(User user, Permission permission) {
        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle(":no_entry_sign: Invalid Permission");
        embed.setColor(ColorPlate.RED);
        embed.setDescription("You need the permission `%s` to use this command".formatted(permission.getName()));
        return embed.build();
    }


}
