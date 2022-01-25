package de.miku.lina.utils;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;

public class PermChecker {

    public static boolean canPermission(Member member, Permission permission) {
        return member.hasPermission(permission);
    }

    public static boolean canBan(Member member) {
        return member.hasPermission(Permission.BAN_MEMBERS);
    }

    public static boolean canKick(Member member) {
        return member.hasPermission(Permission.KICK_MEMBERS);
    }

}
