package de.miku.lina.handlers;

import org.ini4j.Wini;

import java.io.File;
import java.io.IOException;

public class ConfigHandler {

    private final Wini ini;

    public ConfigHandler(String path) throws IOException {
        ini = new Wini(new File(path));
    }

    // core
    public void save() {
        try {
            ini.store();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    // get and set values
    public String getString(String section, String property) {
        return ini.get(section, property);
    }

    public int getInteger(String section, String property) {
        return ini.get(section, property, int.class);
    }

    public boolean getBoolean(String section, String property) {
        return ini.get(section, property, boolean.class);
    }


}
