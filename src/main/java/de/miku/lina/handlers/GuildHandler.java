package de.miku.lina.handlers;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import de.miku.lina.entities.GuildE;
import de.miku.lina.utils.Logging;
import net.dv8tion.jda.api.entities.Guild;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class GuildHandler {

    private Map<String, GuildE> guilds;
    private static final Gson gson = new Gson();

    public GuildHandler() {
        guilds = new HashMap<>();
        JsonReader reader = null;
        try {
            reader = new JsonReader(new FileReader("./guilds.json"));
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

    public GuildE guildAdd(Guild guild) {
        if (guilds.containsKey(guild.getId())) return guilds.get(guild.getId());
        guilds.put(guild.getId(), new GuildE(guild));
        return guilds.get(guild.getId());
    }

    public GuildE getGuild(Guild guild) {
        if (guilds.containsKey(guild.getId())) return guilds.get(guild.getId());
        return guildAdd(guild);
    }

    public void save() {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("./guilds.json"));
            writer.write(gson.toJson(guilds));
            writer.close();
            Logging.info("Successful save guilds");
        } catch (Exception e) {
            String json = gson.toJson(guilds);
            Logging.warning("WARNING SAVING", "CAN'T SAVE GUILDS, try catch it in lastGuilds.txt");
            try {
                FileWriter fileWriter = new FileWriter("./lastGuilds.txt");
                fileWriter.write(json);
                fileWriter.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
        }
    }

}
