package de.miku.lina.handlers;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import de.miku.lina.commands.interactions.InteractionCommand;
import de.miku.lina.utils.ColorPlate;
import de.miku.lina.utils.DataShare;
import de.miku.lina.utils.Interaction;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.reflect.Type;
import java.util.*;

public class InteractionHandler {

    public static MessageEmbed sfwEmbed, nsfwEmbed;

    public InteractionHandler(){

        Gson gson = new Gson();
        JsonReader reader = null;
        JsonReader readerNsfw = null;
        try {
            reader = new JsonReader(new FileReader("./src/main/resources/gifs.json"));
            readerNsfw = new JsonReader(new FileReader("./src/main/resources/gifsNsfw.json"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.exit(0);
        }

        Type type = new TypeToken<Map<String, Interaction>>(){}.getType();
        Map<String, Interaction> myMap = gson.fromJson(reader, type);
        Map<String, Interaction> myMapNsfw = gson.fromJson(readerNsfw, type);
        Map<String, InteractionCommand> sfwCommands = new HashMap<>();
        Map<String, InteractionCommand> nsfwCommands = new HashMap<>();
        for (Map.Entry<String, Interaction> entry: myMap.entrySet()) {
            entry.getValue().setNsfw(false);
            if (myMapNsfw.containsKey(entry.getKey())) {
                entry.getValue().setHasOtherCategoryCommand(true);
            }
            sfwCommands.put(entry.getKey(), entry.getValue().generateCommand());
        }
        for (Map.Entry<String, Interaction> entry: myMapNsfw.entrySet()) {
            entry.getValue().setNsfw(true);
            if (myMap.containsKey(entry.getKey())) {
                entry.getValue().setHasOtherCategoryCommand(true);
            }
            nsfwCommands.put(entry.getKey(), entry.getValue().generateCommand());
        }

        generateEmbeds(myMap.keySet(), myMapNsfw.keySet());
        DataShare.commandHandler.addInteractCommands(sfwCommands, nsfwCommands);
    }

    public void generateEmbeds(Set<String> sfw, Set<String> nsfw) {
        String sfwString = "";
        for (String name: sfw) {
            sfwString += "`%s`, ".formatted(name);
        }
        sfwString = sfwString.substring(0, sfwString.length() - 2);
        String nsfwString = "";
        for (String name: nsfw) {
            nsfwString += "`%s`, ".formatted(name);
        }
        nsfwString = nsfwString.substring(0, nsfwString.length() - 2);
        EmbedBuilder embedRaw = new EmbedBuilder();
        embedRaw.setTitle("Interaction Commands");
        embedRaw.setDescription("Commands are not slash compatible!");
        embedRaw.setColor(ColorPlate.PINK);
        embedRaw.addField("SFW Interactions", sfwString, false);
        sfwEmbed = embedRaw.build();
        embedRaw.setDescription("NSFW commands work only in NSFW channels. Commands are not slash compatible!");
        embedRaw.addField("NSFW Interactions", nsfwString, false);
        nsfwEmbed = embedRaw.build();


    }

    public void addGif(String name, File file, Boolean nsfw) {

    }

    public void removeGif(String name, Integer count, Boolean nsfw) {

    }

    public void createInteraction(String name, String solo, String mention, Boolean nsfw) {

    }

    public void deleteInteraction(String name, Boolean nsfw) {

    }

    public boolean ExitsInteraction(String name, Boolean nsfw) {
        return true;
    }

    public void save() {

    }

}
