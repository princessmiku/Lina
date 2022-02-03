package de.miku.lina.utils;


import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;

public class ReactionRole {

    private String channelId, messageId, roleId, emoji;
    private Role role;

    public ReactionRole(String channelId, String messageId, String roleId, String emoji) {
        this.channelId = channelId;
        this.messageId = messageId;
        this.roleId = roleId;
        this.emoji = emoji;
    }

    public Role getRole(Guild guild) {
        if (role == null)
            role = guild.getRoleById(roleId);
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public void roleSwitch(Member member) {
        if (role == null) {
            role = member.getGuild().getRoleById(roleId);
        }
        if (!member.getGuild().getSelfMember().canInteract(role)) return;
        if (member.getRoles().contains(role))
            removeRole(member);
        else
            addRole(member);
    }

    public void addRole(Member member) {
        if (role == null)
            role = member.getGuild().getRoleById(roleId);
        member.getGuild().addRoleToMember(member, role).queue();
    }

    public void removeRole(Member member) {
        if (role == null)
            role = member.getGuild().getRoleById(roleId);
        member.getGuild().removeRoleFromMember(member, role).queue();
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
