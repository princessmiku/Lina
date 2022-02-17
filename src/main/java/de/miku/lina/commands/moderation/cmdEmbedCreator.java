package de.miku.lina.commands.moderation;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import de.miku.lina.commands.Command;
import de.miku.lina.embed.EmbedJson;
import de.miku.lina.utils.ColorPlate;
import de.miku.lina.utils.DataShare;
import de.miku.lina.utils.DiscordEmbeds;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;
import net.dv8tion.jda.internal.interactions.CommandDataImpl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class cmdEmbedCreator extends Command {
    public cmdEmbedCreator() {
        super("embed", "Create your own embed");
        commandActive = true;
        messageCompatible = true;
        slashCompatible = true;
    }

    @Override
    protected void generateCommandData() {
        commandData = new CommandDataImpl(name, description)
                .addSubcommands(new SubcommandData("new", "create a new embed").addOption(OptionType.CHANNEL, "channel", "set the embed channel"))
                .addSubcommands(new SubcommandData("edit", "edit a embed").addOption(OptionType.CHANNEL, "channel", "set the embed channel").addOption(OptionType.STRING, "messageid", "set the id of the edit message"));
    }

    @Override
    public void onSlash(SlashCommandInteractionEvent event) {
        event.deferReply().queue();
    }

    @Override
    public void onMessage(MessageReceivedEvent event, String[] args) {
        //if (!event.getMember().hasPermission(Permission.ADMINISTRATOR)) {
        //    event.getMessage().reply(DiscordEmbeds.invalidPermission(event.getAuthor(), Permission.ADMINISTRATOR)).queue();
        //    return;
        //}
        if (event.getMessage().getMentionedChannels().isEmpty()) {
            EmbedBuilder embedBuilder = new EmbedBuilder();
            embedBuilder.setTitle("Embed creator tutorial");
            embedBuilder.setDescription("""
                    How to create a embed? It's easy, [click me!](https://glitchii.github.io/embedbuilder/)

                    **Tutorial:**
                    **1** Create your embed on the web page. *Tip: Use the broom to delete the content.*
                    **2** Change top left from `Gui` to `Json`.
                    **3.** Copy the content
                    **4.** Execute this command again with channel specifying `embed [channel]`, you can also edit one using `embed edit [channel] [messageId]`. Or use the slash commands
                    **5.** Send the JSON to the chat, this can be done as a message or if the characters are not enough as a file. The file must be called `message.txt`, this will be created automatically by discord.
                    **6.** Done, the embed has now been sent to the specified channel.""");
            embedBuilder.setColor(ColorPlate.BLUE);
            event.getMessage().replyEmbeds(embedBuilder.build()).queue();
            return;
        }
        if (args[0].equalsIgnoreCase("edit")) {
            event.getMessage().reply("*feature not included*").queue();
            return;
        }

        TextChannel channel = event.getMessage().getMentionedChannels().get(0);
        Member self = event.getGuild().getSelfMember();

        if (!self.hasPermission(channel, Permission.MESSAGE_SEND)) {
            event.getMessage().replyEmbeds(DiscordEmbeds.invalidSelfPermission(Permission.MESSAGE_SEND)).queue();
            return;
        }
        if (!self.hasPermission(channel, Permission.MESSAGE_EMBED_LINKS)) {
            event.getMessage().replyEmbeds(DiscordEmbeds.invalidSelfPermission(Permission.MESSAGE_EMBED_LINKS)).queue();
            return;
        }
        if (!event.getMember().hasPermission(channel, Permission.MESSAGE_SEND)) {
            event.getMessage().replyEmbeds(DiscordEmbeds.invalidPermission(event.getAuthor(), Permission.MESSAGE_SEND)).queue();
            return;
        }

        event.getMessage().replyEmbeds(new EmbedBuilder().setDescription("Send the json as text or as file, discord will create it").setColor(ColorPlate.BLUE).build()).queue();
        DataShare.eventWaiter.waitForEvent(MessageReceivedEvent.class, event1 -> {
            if (event.getMember().getId().equals(event1.getMember().getId()) && event.getChannel().getId().equals(event1.getChannel().getId()) && !event.getMessage().getId().equals(event1.getMessageId())) return true;
            return false;
        }, e -> {
            Message message = e.getMessage();
            String rawJson = "";
            if (!message.getAttachments().isEmpty()) {
                Message.Attachment attachment = message.getAttachments().get(0);
                if (!attachment.getFileName().equals("message.txt")) {
                    event.getMessage().replyEmbeds(DiscordEmbeds.error(event.getAuthor(), "Wrong file")).queue();
                    return;
                }
                File file = attachment.downloadToFile("%sembed.txt".formatted(event.getAuthor().getId())).join();
                try {
                    Scanner myReader = new Scanner(file);
                    while (myReader.hasNextLine()) {
                        rawJson += myReader.nextLine();
                    }
                    myReader.close();
                    file.delete();
                } catch (FileNotFoundException err) {
                    file.delete();
                }
            } else {
                rawJson = message.getContentRaw();
            }
            JsonObject object;
            try {
                object = (JsonObject) JsonParser.parseString(rawJson);
            } catch (Exception err) {
                event.getMessage().replyEmbeds(DiscordEmbeds.error(event.getAuthor(),"this is not a json valid format")).queue();
                return;
            }
            if (object.has("embed")) {
                try {
                    event.getMessage().getMentionedChannels().get(0).sendMessageEmbeds(EmbedJson.jsonToEmbed(object.getAsJsonObject("embed"))).complete();
                    event.getMessage().replyEmbeds(DiscordEmbeds.success(event.getAuthor(), "Embed created")).queue();
                } catch (Exception err) {
                    event.getMessage().replyEmbeds(DiscordEmbeds.error(event.getAuthor(),"Wrong embed / json format")).queue();
                    return;
                }
            } else {
                event.getMessage().replyEmbeds(DiscordEmbeds.error(event.getAuthor(),"No embed found")).queue();
        }}, 1, TimeUnit.MINUTES, () -> {
            event.getMessage().replyEmbeds(DiscordEmbeds.error(event.getAuthor(), "Time out...")).queue();
        });
    }
}
