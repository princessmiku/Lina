package de.miku.lina.commands.moderation;

import de.miku.lina.commands.Command;
import de.miku.lina.utils.ColorPlate;
import de.miku.lina.utils.DiscordEmbeds;
import de.miku.lina.utils.StringChecker;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

import javax.xml.crypto.Data;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class cmdClear extends Command {

    public cmdClear() {
        super("clear", "Clean the chat like Mr. Clean, or also known as Meister Proper");
        commandActive = true;
        messageCompatible = true;
        slashCompatible = true;
    }

    @Override
    protected void generateCommandData() {
        commandData = new CommandData(name, description).addOption(
                OptionType.INTEGER, "amount", "how many messages", true
        );
    }

    @Override
    public void onSlash(SlashCommandEvent event) {
        if (!event.getGuild().getSelfMember().hasPermission(event.getTextChannel(), Permission.MESSAGE_MANAGE)) {
            event.replyEmbeds(DiscordEmbeds.invalidSelfPermission(Permission.MESSAGE_MANAGE)).queue();
            return;
        }
        if (!event.getMember().hasPermission(event.getTextChannel(), Permission.MESSAGE_MANAGE)) {
            event.replyEmbeds(DiscordEmbeds.invalidPermission(event.getUser(), Permission.MESSAGE_MANAGE)).queue();
            return;
        }
        int howMany = Integer.parseInt(event.getOptions().get(0).getAsString());
        if (howMany < 2) {
            event.replyEmbeds(DiscordEmbeds.invalidInput(event.getUser(), "amount is 1 or smaller")).queue();
            return;
        }
        List<Message> messages = event.getChannel().getHistory().retrievePast(howMany).complete();
        event.getTextChannel().deleteMessages(messages).queue();
        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle(":wastebasket: Deleted message/s");
        embed.setDescription("I have cleaned up `%s` messages here".formatted(String.valueOf(messages.size())));
        embed.setColor(ColorPlate.GREEN);
        event.replyEmbeds(embed.build()).queue(m -> m.deleteOriginal().queueAfter(5, TimeUnit.SECONDS));
    }

    @Override
    public void onMessage(MessageReceivedEvent event, String[] args) {
        if (args.length != 1) {
            event.getMessage().reply(DiscordEmbeds.noInput(event.getAuthor(), "amount")).queue();
            return;
        }
        String rawArg = args[0];
        if (!StringChecker.isInteger(rawArg)) {
            event.getMessage().reply(DiscordEmbeds.invalidInput(event.getAuthor(), "amount")).queue();
            return;
        }
        if (!event.getGuild().getSelfMember().hasPermission(event.getTextChannel(), Permission.MESSAGE_MANAGE)) {
            event.getMessage().reply(DiscordEmbeds.invalidSelfPermission(Permission.MESSAGE_MANAGE)).queue();
            return;
        }
        if (!event.getMember().hasPermission(event.getTextChannel(), Permission.MESSAGE_MANAGE)) {
            event.getMessage().reply(DiscordEmbeds.invalidPermission(event.getAuthor(), Permission.MESSAGE_MANAGE)).queue();
            return;
        }
        List<Message> messages = event.getChannel().getHistory().retrievePast(Integer.parseInt(rawArg) + 1).complete();
        event.getTextChannel().deleteMessages(messages).queue();
        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle(":wastebasket: Deleted message/s");
        embed.setDescription("I have cleaned up `%s` messages here".formatted(String.valueOf(messages.size())));
        embed.setColor(ColorPlate.GREEN);
        event.getChannel().sendMessage(embed.build()).queue(m -> m.delete().queueAfter(5, TimeUnit.SECONDS));

    }

}
