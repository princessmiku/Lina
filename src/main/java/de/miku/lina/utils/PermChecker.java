package de.miku.lina.utils;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.GuildChannel;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class PermChecker {

    // TODO: at time times revise and improve

    public static List<Permission> checkForPermissionsWithReturnList(Member member, GuildChannel channel, List<Permission> permissions) {
        List<Permission> denyPerms = new ArrayList<>();
        for (Permission perm: permissions) {
            if(perm.isChannel()) {
                if(!member.hasPermission(channel, perm)) denyPerms.add(perm);
            } else if (perm.isGuild()) {
                if(!member.hasPermission(perm)) denyPerms.add(perm);
            } else {
                denyPerms.add(perm);
            }
        }
        return denyPerms;
    }
    public static boolean checkForPermissions(Member member, GuildChannel channel, List<Permission> permissions) {
        List<Permission> denyPerms = checkForPermissionsWithReturnList(member, channel, permissions);
        return denyPerms.isEmpty();
    }

    public static boolean handlePermissionCheck(SlashCommandInteractionEvent event, List<Permission> permissions) {
        List<Permission> denyPerms = checkForPermissionsWithReturnList(event.getMember(), event.getGuildChannel(), permissions);
        if (denyPerms.isEmpty()) return true;
        // Build the embed
        EmbedBuilder invalidPermission = new EmbedBuilder();
        invalidPermission.setTitle("Du hast nicht genug Rechte");
        // build the "error" string
        String denyPermsString = "";
        for (Permission p: denyPerms) {
            denyPermsString += "`%s`".formatted(p.getName());
        }
        invalidPermission.setDescription("Diese Rechte fehlen dir " + denyPermsString);
        invalidPermission.setColor(Color.RED); // TODO: Implemented color system
        // reply error and delete it after 5 seconds
        event.replyEmbeds(invalidPermission.build()).queue(message -> message.deleteOriginal().queueAfter(5, TimeUnit.SECONDS));
        return false;
    }



    public static boolean canPermission(Member member, Permission permission) {
        return member.hasPermission(permission);
    }

    public static boolean canBan(Member member) {
        return member.hasPermission(Permission.BAN_MEMBERS);
    }

    public static boolean canKick(Member member) {
        return member.hasPermission(Permission.KICK_MEMBERS);
    }

    public static boolean canManageRoles(Member member) {
        return member.hasPermission(Permission.MANAGE_ROLES);
    }

    public static boolean canManageMessage(Member member, GuildChannel channel) {
        return member.hasPermission(channel, Permission.MESSAGE_MANAGE);
    }

}
