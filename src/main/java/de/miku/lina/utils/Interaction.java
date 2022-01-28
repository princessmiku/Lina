package de.miku.lina.utils;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class Interaction {

    private Map<String, String> gifs;
    private String name, mono, mention;
    private boolean nsfw;

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
        Random random = new Random();
        return gifs.get(random.nextInt(gifs.size() - 1));
    }

    public Map<String, String> getGifs() {
        return gifs;
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
}
