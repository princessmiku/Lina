package de.miku.lina.commands.moderation;

import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import de.miku.lina.commands.Command;
import de.miku.lina.utils.ColorPlate;
import de.miku.lina.utils.DataShare;
import de.miku.lina.utils.DiscordEmbeds;
import de.miku.lina.utils.ReactionRole;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.exceptions.ContextException;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

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
        commandData = new CommandData(name, description).addOption(OptionType.ROLE, "role", "Add to a message the desired reaction, which should be the reaction message");
    }

    @Override
    public void onSlash(SlashCommandEvent event) {
        Role role = event.getOptions().get(0).getAsRole();
        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle("New reaction role");
        embed.setDescription("Add to a message the desired reaction, which should be the reaction message for the role `%s`".formatted(role.getName()));
        embed.setColor(ColorPlate.BLUE);
        event.replyEmbeds(embed.build()).queue(
                message -> DataShare.eventWaiter.waitForEvent(
                        MessageReactionAddEvent.class, e -> {
                            if (e.getMember() != event.getMember())
                                return false;
                            return true;
                        },
                        evr -> {
                            MessageReactionAddEvent e = (MessageReactionAddEvent) evr;
                            String messageId = e.getMessageId();
                            String channelId = e.getChannel().getId();
                            String emoji = e.getReactionEmote().toString();
                            e.getTextChannel().retrieveMessageById(messageId).queue( msg -> {
                                if (msg == null) {
                                    message.editOriginalEmbeds(DiscordEmbeds.error(event.getUser(), "message not found")).queue();
                                    return;
                                }
                                try {
                                    Objects.requireNonNull(msg).removeReaction(e.getReactionEmote().getEmote(), Objects.requireNonNull(e.getUser())).queue();
                                } catch (IllegalStateException error) {
                                    Objects.requireNonNull(msg).removeReaction(e.getReactionEmote().getEmoji(), Objects.requireNonNull(e.getUser())).queue();
                                }
                                try {
                                    msg.addReaction(e.getReactionEmote().getEmote()).queue(msg1 -> {
                                        EmbedBuilder embedBuilder = new EmbedBuilder();
                                        try {
                                            embedBuilder.setDescription(
                                                    "Reaction successfully created!\n\nRole: %s\nChannel: %s\nMessage ID: `%s`\nEmoji: %s".formatted(
                                                            role.getAsMention(), e.getTextChannel().getAsMention(), messageId, e.getReactionEmote().getEmote()));
                                        } catch (IllegalStateException error1) {
                                            embedBuilder.setDescription(
                                                    "Reaction successfully created!\n\nRole: %s\nChannel: %s\nMessage ID: `%s`\nEmoji: %s".formatted(
                                                            role.getAsMention(), e.getTextChannel().getAsMention(), messageId, e.getReactionEmote().getEmoji()));
                                        }
                                        embedBuilder.setColor(ColorPlate.GREEN);
                                        embedBuilder.setTitle("Created!");
                                        DataShare.guildHandler.getGuild(event.getGuild()).addReactionRole(new ReactionRole(channelId, messageId, role.getId(), emoji));
                                        DataShare.guildHandler.save();
                                        message.editOriginalEmbeds(embedBuilder.build()).queueAfter(5, TimeUnit.SECONDS, message1 -> {
                                            message1.delete().queue();
                                        });
                                    });
                                }  catch (IllegalStateException error) {
                                    try {
                                        msg.addReaction(e.getReactionEmote().getEmoji()).queue(msg1 -> {
                                            EmbedBuilder embedBuilder = new EmbedBuilder();
                                            try {
                                                embedBuilder.setDescription(
                                                        "Reaction successfully created!\n\nRole: %s\nChannel: %s\nMessage ID: `%s`\nEmoji: %s".formatted(
                                                                role.getAsMention(), e.getTextChannel().getAsMention(), messageId, e.getReactionEmote().getEmote()));
                                            } catch (IllegalStateException error1) {
                                                embedBuilder.setDescription(
                                                        "Reaction successfully created!\n\nRole: %s\nChannel: %s\nMessage ID: `%s`\nEmoji: %s".formatted(
                                                                role.getAsMention(), e.getTextChannel().getAsMention(), messageId, e.getReactionEmote().getEmoji()));
                                            }
                                            embedBuilder.setColor(ColorPlate.GREEN);
                                            embedBuilder.setTitle("Created!");
                                            DataShare.guildHandler.getGuild(event.getGuild()).addReactionRole(new ReactionRole(channelId, messageId, role.getId(), emoji));
                                            DataShare.guildHandler.save();
                                            message.editOriginalEmbeds(embedBuilder.build()).queueAfter(5, TimeUnit.SECONDS, message1 -> {
                                                message1.delete().queue();
                                            });
                                        });
                                    } catch (IllegalAccessError error1) {
                                        message.editOriginalEmbeds(DiscordEmbeds.error(event.getUser(), "I can't add this emoji")).queue();
                                        return;
                                    }
                                } catch (Exception error) {
                                    message.editOriginalEmbeds(DiscordEmbeds.error(event.getUser(), "I can't add this emoji")).queue();
                                    return;
                                }

                            }
                            );

                        },
                        1, TimeUnit.MINUTES,
                        () -> {
                            message.editOriginalEmbeds(DiscordEmbeds.error(event.getUser(), "You didn't respond in time!")).queue();
                        }
                        ));
    }

    @Override
    public void onMessage(MessageReceivedEvent event, String[] args) {

    }
}
