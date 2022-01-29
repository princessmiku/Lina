package de.miku.lina.commands.interactions;

import de.miku.lina.commands.Command;
import de.miku.lina.utils.ColorPlate;
import de.miku.lina.utils.DataShare;
import de.miku.lina.utils.Interaction;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class InteractionCommand{

    private Interaction interaction;
    protected String name, description, category;

    public InteractionCommand(String name, Interaction interaction) {
        this.name = name;
        this.description = "interact with someone";
        this.interaction = interaction;
        this.category = "sfw";
    }

    public void onMessage(MessageReceivedEvent event) {
        String toFill = "";
        List<Member> mentions = event.getMessage().getMentionedMembers();
        if (mentions.isEmpty()) {
            if (!interaction.getMono().equalsIgnoreCase("none")) {
                toFill = interaction.getMono().replaceAll("\\{author\\}", "**" + event.getAuthor().getName() + "**");
            }
            else {
                toFill = interaction.getMention().replaceAll("\\{author\\}", "**" + event.getAuthor().getName() + "**").replaceAll("\\{mention\\}", "**" + DataShare.jda.getSelfUser().getName() + "**");
            }
        } else {
            String names = "";
            int count = 0;
            for (Member m: mentions) {
                names += "%s, ".formatted(m.getEffectiveName());
                count++;
                if (count >= 5) break;
            }
            toFill = interaction.getMention().replaceAll("\\{author\\}", "**" + event.getAuthor().getName() + "**").replaceAll("\\{mention\\}", "**" + names.substring(0, names.length() - 2) + "**");
        }
        event.getMessage().reply(new EmbedBuilder().setDescription(toFill).setImage(interaction.getRandomGif()).setColor(ColorPlate.PINK).build()).queue();
    }

}
