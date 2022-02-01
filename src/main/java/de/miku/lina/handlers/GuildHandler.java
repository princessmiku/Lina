package de.miku.lina.handlers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import de.miku.lina.entities.GuildE;
import de.miku.lina.utils.Interaction;
import de.miku.lina.utils.Logging;
import net.dv8tion.jda.api.entities.Guild;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class GuildHandler {

    private Map<String, GuildE> guilds;

    public GuildHandler() {
        guilds = new HashMap<>();
        Gson gson = new Gson();
        JsonReader reader = null;
        try {
            reader = new JsonReader(new FileReader("./src/main/resources/guilds.json"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.exit(0);
        }
        Type type = new TypeToken<Map<String, GuildE>>(){}.getType();
        Map<String, GuildE> raw = gson.fromJson(reader, type);
        if (raw != null)
            guilds.putAll(raw);

        try {
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void checkGuild(@NotNull Guild guild) {
        if (guilds.containsKey(guild.getId())) return;
        guilds.put(guild.getId(), new GuildE(guild));
    }

    public void save() {
        Gson gson = new GsonBuilder().enableComplexMapKeySerialization().setPrettyPrinting()
                .create();
        try {
            Writer writer = new FileWriter("./src/main/resources/guilds.json");
            gson.toJson(guilds, writer);
            writer.close();
        } catch (IOException e) {
            Logging.warning("WARNING SAVING", gson.toJson(guilds));
            e.printStackTrace();
        }
    }

}
