package de.miku.lina.commands.moderation;

import de.miku.lina.commands.Command;
import de.miku.lina.utils.ColorPlate;
import de.miku.lina.utils.DataShare;
import de.miku.lina.utils.DiscordEmbeds;
import de.miku.lina.utils.PermChecker;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

import java.awt.*;
import java.util.ArrayList;


public class cmdBan extends Command {
    public cmdBan() {
        super("ban", "Ban a user or a mass of members, in slash mode is only possible to ban one member per time. Mention the Member/s to ban");
        commandActive = true;
        messageCompatible = true;
        slashCompatible = true;

    }

    @Override
    protected void generateCommandData() {
        commandData = new CommandData(name, "Ban a member from your guild.").addOption(
                OptionType.USER, "member", "select user to ban", true
        );
    }

    @Override
    public void onSlash(SlashCommandEvent event) {
        Member selfMember = event.getGuild().getSelfMember();
        if (!PermChecker.canBan(selfMember)) {
            event.replyEmbeds(DiscordEmbeds.invalidSelfPermission(Permission.BAN_MEMBERS)).queue();
            return;
        }
        if (!PermChecker.canBan(event.getMember())) {
            event.replyEmbeds(DiscordEmbeds.invalidPermission(event.getUser(), Permission.BAN_MEMBERS)).queue();
            return;
        }
        if (event.getOptions().isEmpty()) {
            event.replyEmbeds(DiscordEmbeds.noMention(event.getMember().getUser())).queue();
            return;
        }
        Member toBayMember = event.getOptions().get(0).getAsMember();
        if (!selfMember.canInteract(toBayMember) || !event.getMember().canInteract(toBayMember)) {
            event.replyEmbeds(DiscordEmbeds.error(event.getUser(), "You can't ban him, that's evil, I don't like evil, if you want to ban him, subjugate him first. below me.... and you....")).queue();
            return;
        }
        String name = toBayMember.getEffectiveName();
        try {
            toBayMember.ban(1, "Ban with slash command by " + event.getMember().getEffectiveName() + " / " + event.getMember().getId()).queue();
        } catch (Exception e) {
            event.replyEmbeds(DiscordEmbeds.internalError()).queue();
            return;
        }

        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle(":police_car: Successfully ban");
        embed.setColor(ColorPlate.GREEN);
        embed.setDescription("I banned the Member " + name);
        event.replyEmbeds(embed.build()).queue();

    }

    @Override
    public void onMessage(MessageReceivedEvent event, String[] args) {
        Member selfMember = event.getGuild().getSelfMember();
        if (!PermChecker.canBan(selfMember)) {
            event.getMessage().reply(DiscordEmbeds.invalidSelfPermission(Permission.BAN_MEMBERS)).queue();
            return;
        }
        if (!PermChecker.canBan(event.getMember())) {
            event.getMessage().reply(DiscordEmbeds.invalidPermission(event.getAuthor(), Permission.BAN_MEMBERS)).queue();
            return;
        }
        if (event.getMessage().getMentionedMembers().isEmpty()) {
            event.getMessage().reply(DiscordEmbeds.noMention(event.getAuthor())).queue();
            return;
        }
        ArrayList<Member> cantBanMembers = new ArrayList<>();

        for (Member member: event.getMessage().getMentionedMembers()) {
            try {
                if (!selfMember.canInteract(member) || !event.getMember().canInteract(member)) {
                    cantBanMembers.add(member);
                    continue;
                }
                event.getGuild().ban(member, 1, "Ban with command by " + event.getMember().getEffectiveName() + " / " + event.getMember().getId()).queue();
            } catch (Exception e) {
                cantBanMembers.add(member);
            }
        }
        String cantBanStr = "";
        if (!cantBanMembers.isEmpty()) {
            cantBanStr += "\n\nBut i can't ban follow members\n";
            for (Member member: cantBanMembers) {
                cantBanStr += "- " + member.getAsMention() + "\n";
            }
        }
        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle(":police_car: Successfully ban");
        embed.setColor(ColorPlate.GREEN);
        embed.setDescription("I banned the mentioned members" + cantBanStr);
        embed.setFooter("Banned %s/%s".formatted(String.valueOf(event.getMessage().getMentionedMembers().size() - cantBanMembers.size()), String.valueOf(event.getMessage().getMentionedMembers().size())));
        event.getMessage().reply(embed.build()).queue();

    }


}
