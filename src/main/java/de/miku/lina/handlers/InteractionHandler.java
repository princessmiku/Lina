package de.miku.lina.handlers;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import de.miku.lina.utils.Interaction;
import net.dv8tion.jda.api.entities.User;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.*;

public class InteractionHandler {

    private HashMap<String, Interaction> sfwInter, nsfwInter; // list of gifs

    public InteractionHandler(){
        sfwInter = new HashMap<>();
        nsfwInter = new HashMap<>();

        File file = new File("./gifs.json");
        Gson gson = new Gson();
        JsonReader reader = null;
        JsonReader readerNsfw = null;
        try {
            reader = new JsonReader(new FileReader("./gifs.json"));
            readerNsfw = new JsonReader(new FileReader("./gifsNsfw.json"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.exit(0);
        }
        Type type = new TypeToken<Map<String, Interaction>>(){}.getType();
        Map<String, Interaction> myMap = gson.fromJson(reader, type);
        for (Map.Entry<String, Interaction> entry: myMap.entrySet()) {
            entry.getValue().setNsfw(false);
            System.out.println(entry.getValue().getName());
        }
        Map<String, Interaction> myMapNsfw = gson.fromJson(readerNsfw, type);
        for (Map.Entry<String, Interaction> entry: myMapNsfw.entrySet()) {
            entry.getValue().setNsfw(true);
        }
        sfwInter.putAll(myMap);
        nsfwInter.putAll(myMapNsfw);
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
