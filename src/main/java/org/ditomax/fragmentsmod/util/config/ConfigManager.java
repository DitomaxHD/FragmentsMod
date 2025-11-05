package org.ditomax.fragmentsmod.util.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class ConfigManager {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final File CONFIG_FILE = new File(FabricLoader.getInstance().getConfigDir().toFile(), "fragmentsmod/fragmentsmod.json");

    public static void loadConfig() {
        if (CONFIG_FILE.exists()) {
            try (FileReader reader = new FileReader(CONFIG_FILE)) {
                ModConfig loadedConfig = GSON.fromJson(reader, ModConfig.class);
                if (loadedConfig != null) {
                    ModConfig.setLocalInstance(loadedConfig);
                } else {
                    saveConfig();
                }
            } catch (IOException e) {
                System.err.println("Could not read config file!");
                e.printStackTrace();
            }
        } else {
            saveConfig();
        }
    }

    public static void saveConfig() {
        CONFIG_FILE.getParentFile().mkdirs();
        try (FileWriter writer = new FileWriter(CONFIG_FILE)) {
            GSON.toJson(ModConfig.getLocalInstance(), writer);
        } catch (IOException e) {
            System.err.println("Could not write config file!");
            e.printStackTrace();
        }
    }
}