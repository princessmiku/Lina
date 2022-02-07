package de.miku.lina.commands.moderation;

import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import de.miku.lina.commands.Command;
import de.miku.lina.utils.ColorPlate;
import de.miku.lina.utils.DataShare;
import de.miku.lina.utils.DiscordEmbeds;
import de.miku.lina.utils.ReactionRole;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageReaction;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.exceptions.ContextException;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

import java.awt.*;
import java.util.Collection;
import java.util.Collections;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class cmdAddReactionRole extends Command {
    public cmdAddReactionRole() {
        super("addreactionrole", "add a role that given, when someone interact with a reaction");
        commandActive = true;
        slashCompatible = true;
    }

    @Override
    protected void generateCommandData() {
        commandData = new CommandData(name, description).addOption(OptionType.ROLE, "role", "Add to a message the desired reaction, which should be the reaction message", true);
    }

    @Override
    public void onSlash(SlashCommandEvent event) {
        Role role = event.getOptions().get(0).getAsRole();
        if (!event.getGuild().getSelfMember().canInteract(role)) {
            event.replyEmbeds(DiscordEmbeds.error(event.getUser(), "I can't interact with this role")).queue();
            return;
        }
        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle("New reaction role");
        embed.setDescription("Add to a message the desired reaction, which should be the reaction message for the role `%s`".formatted(role.getName()));
        embed.setColor(ColorPlate.BLUE);
        event.replyEmbeds(embed.build()).queue(
                msg -> {
                        DataShare.eventWaiter.waitForEvent(MessageReactionAddEvent.class, e -> {
                        if(e.getMember() != event.getMember() && e.getGuild() != event.getGuild()) return false;
                        return true;
                    }, e -> {
                            Member self = event.getGuild().getSelfMember();
                            if (!self.hasPermission(event.getTextChannel(),Permission.MESSAGE_MANAGE)) {
                                msg.editOriginalEmbeds(DiscordEmbeds.invalidSelfPermission(Permission.MESSAGE_MANAGE)).queue(errorMSG -> errorMSG.delete().queueAfter(5, TimeUnit.SECONDS));
                                return;
                            }
                            if (!self.hasPermission(event.getTextChannel(),Permission.MESSAGE_ADD_REACTION)) {
                                msg.editOriginalEmbeds(DiscordEmbeds.invalidSelfPermission(Permission.MESSAGE_ADD_REACTION)).queue(errorMSG -> errorMSG.delete().queueAfter(5, TimeUnit.SECONDS));
                                return;
                            }
                            String messageId = e.getMessageId(), channelId = e.getChannel().getId(), emote = e.getReactionEmote().toString();
                            Message message = e.getChannel().retrieveMessageById(messageId).complete();
                            MessageReaction.ReactionEmote reaction = e.getReaction().getReactionEmote();
                            boolean isEmote = false;
                            DataShare.ignoreReactions.add(e.getUserId());
                            try {
                                message.removeReaction(reaction.getEmoji(), event.getUser()).queue();
                            } catch (IllegalStateException error) {
                                message.removeReaction(reaction.getEmote(), event.getUser()).queue();
                                isEmote = true;
                            } catch (Exception error) {
                                msg.editOriginalEmbeds(DiscordEmbeds.internalError()).queue(errorMSG -> errorMSG.delete().queueAfter(5, TimeUnit.SECONDS));
                                DataShare.ignoreReactions.remove(e.getUserId());
                                return;
                            }
                            try {
                                if (!isEmote) {
                                    message.addReaction(reaction.getEmoji()).complete();
                                } else {
                                    if (!reaction.getEmote().isAvailable()) {
                                        msg.editOriginalEmbeds(DiscordEmbeds.error(event.getUser(), "I can't use this emoji")).queue(errorMSG -> errorMSG.delete().queueAfter(5, TimeUnit.SECONDS));
                                        return;
                                    }
                                    message.addReaction(reaction.getEmote()).complete();
                                }
                            } catch (Exception error) {
                                msg.editOriginalEmbeds(DiscordEmbeds.internalError()).queue(errorMSG -> errorMSG.delete().queueAfter(5, TimeUnit.SECONDS));
                                return;
                            }
                            String embedString = "Reaction successfully created! Role: %s\nChannel: %s\nMessage: %s\nEmoji: %s";
                            if (isEmote)
                                embedString = embedString.formatted(role.getAsMention(), e.getTextChannel().getAsMention(), message.getJumpUrl(), reaction.getEmote());
                            else
                                embedString = embedString.formatted(role.getAsMention(), e.getTextChannel().getAsMention(), message.getJumpUrl(), reaction.getEmoji());
                            DataShare.guildHandler.getGuild(event.getGuild()).addReactionRole(new ReactionRole(channelId, messageId, role.getId(), emote));
                            DataShare.guildHandler.save();
                            EmbedBuilder builder = new EmbedBuilder();
                            builder.setTitle("Creation Successful");
                            builder.setDescription(embedString);
                            builder.setColor(ColorPlate.BLUE);
                            msg.editOriginalEmbeds(builder.build()).queue(errorMSG -> errorMSG.delete().queueAfter(8, TimeUnit.SECONDS));
                    }, 2, TimeUnit.MINUTES, () -> {
                            msg.editOriginalEmbeds(DiscordEmbeds.error(event.getUser(), "Time out...")).queue(errorMSG -> errorMSG.delete().queueAfter(5, TimeUnit.SECONDS));
                            return;
                        });

                }
        );
    }

    @Override
    public void onMessage(MessageReceivedEvent event, String[] args) {

    }
}
