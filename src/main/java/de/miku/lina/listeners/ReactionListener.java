package de.miku.lina.listeners;

import de.miku.lina.entities.GuildE;
import de.miku.lina.utils.DataShare;
import de.miku.lina.utils.ReactionRole;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class ReactionListener extends ListenerAdapter {

    @Override
    public void onMessageReactionAdd(@NotNull MessageReactionAddEvent event) {
        if (event.getUserId().equals(DataShare.jda.getSelfUser().getId())) return;
        if (DataShare.ignoreReactions.contains(event.getUserId())) {
            DataShare.ignoreReactions.remove(event.getUserId());
            return;
        }
        GuildE guildE = DataShare.guildHandler.getGuild(event.getGuild());
        ReactionRole reactionRole = guildE.getReactionRole(event.getChannel().getId(), event.getMessageId(), event.getReactionEmote().toString());
        if (reactionRole != null) {
            reactionRole.roleSwitch(Objects.requireNonNull(event.getMember()));
            if (event.getGuild().getSelfMember().hasPermission(event.getTextChannel(), Permission.MESSAGE_MANAGE)) event.getReaction().removeReaction(event.getUser()).queue();
        }
    }
}
