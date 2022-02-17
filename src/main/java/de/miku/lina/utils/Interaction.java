package de.miku.lina.utils;

import de.miku.lina.commands.Command;
import de.miku.lina.commands.interactions.InteractionCommand;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Random;


public class Interaction {

    private Map<String, String> gifs;
    private String name, mono, mention;
    private boolean nsfw, hasOtherCategoryCommand;

    public Interaction(String name, String mono, String mention, Map<String, String> gifs) {
        this.name = name;
        this.mono = mono;
        this.mention = mention;
        this.gifs = gifs;
        this.nsfw = true;
    }

    public void addGif(File file) {
        String url = "";

        gifs.put("url", "");
    }

    public String getRandomGif() {
        Random generator = new Random();
        Object[] values = gifs.values().toArray();
        String randomValue = (String) values[generator.nextInt(values.length)];
        return randomValue;
    }

    public Map<String, String> getGifs() {
        return gifs;
    }

    public InteractionCommand generateCommand() {
        return new InteractionCommand(name, this);
    }

    public String getName() {
        return name;
    }

    public String getMono() {
        return mono;
    }

    public String getMention() {
        return mention;
    }

    public void setNsfw(boolean nsfw) {
        this.nsfw = nsfw;
    }

    public boolean isNsfw() {
        return nsfw;
    }

    public boolean isHasOtherCategoryCommand() {
        return hasOtherCategoryCommand;
    }

    public void setHasOtherCategoryCommand(boolean hasOtherCategoryCommand) {
        this.hasOtherCategoryCommand = hasOtherCategoryCommand;
    }
}
