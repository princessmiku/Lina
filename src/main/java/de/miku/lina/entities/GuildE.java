package de.miku.lina.entities;

import de.miku.lina.utils.ReactionRole;
import net.dv8tion.jda.api.entities.Guild;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class GuildE implements Serializable {

    private String _id, name, createdAt;
    private Map<String, Map<String, Map<String, ReactionRole>>> reactionRoles;

    public GuildE(@NotNull Guild guild) {
        _id = guild.getId();
        name = guild.getName();
        reactionRoles = new HashMap<>();
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();
        createdAt = formatter.format(date);
    }

    public void addReactionRole(@NotNull ReactionRole reactionRole) {
        if (!reactionRoles.containsKey(reactionRole.getChannelId()))
            reactionRoles.put(reactionRole.getChannelId(), new HashMap<>());
        if (!reactionRoles.get(reactionRole.getChannelId()).containsKey(reactionRole.getMessageId()))
            reactionRoles.get(reactionRole.getChannelId()).put(reactionRole.getMessageId(), new HashMap<>());
        if (!reactionRoles.get(reactionRole.getChannelId()).get(reactionRole.getMessageId()).containsKey(reactionRole.getEmoji()))
            reactionRoles.get(reactionRole.getChannelId()).get(reactionRole.getMessageId()).put(reactionRole.getEmoji(), reactionRole);
    }

    public ReactionRole getReactionRole(String channelId, String messageId, String emoji) {
        if (!reactionRoles.containsKey(channelId))
            return null;
        if (!reactionRoles.get(channelId).containsKey(messageId))
            return null;
        if (!reactionRoles.get(channelId).get(messageId).containsKey(emoji))
            return null;
        return reactionRoles.get(channelId).get(messageId).get(emoji);
    }



}
