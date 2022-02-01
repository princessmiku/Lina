package de.miku.lina.utils;


import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;

public class ReactionRole {

    private String channelId, messageId, roleId, emoji;

    public ReactionRole(String channelId, String messageId, String roleId, String emoji) {
        this.channelId = channelId;
        this.messageId = messageId;
        this.roleId = roleId;
        this.emoji = emoji;
    }

    public void addRole(Member member) {
        Guild guild = member.getGuild();
        if (!guild.getSelfMember().canInteract(member)) return;
        Role role = guild.getRoleById(roleId);
        if (role == null) return;
        if (member.getRoles().contains(role)) return;
        guild.addRoleToMember(member, role).queue();
    }

    public void removeRole(Member member) {
        Guild guild = member.getGuild();
        if (!guild.getSelfMember().canInteract(member)) return;
        Role role = guild.getRoleById(roleId);
        if (role == null) return;
        if (!member.getRoles().contains(role)) return;
        guild.removeRoleFromMember(member, role).queue();
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getEmoji() {
        return emoji;
    }

    public void setEmoji(String emoji) {
        this.emoji = emoji;
    }
}
