package com.tristankechlo.whatdidijustkill.config;

import com.google.gson.*;
import com.google.gson.stream.JsonWriter;
import com.tristankechlo.whatdidijustkill.IPlatformHelper;
import com.tristankechlo.whatdidijustkill.WhatDidIJustKill;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.concurrent.atomic.AtomicBoolean;

public final class ConfigManager {

    private static final File CONFIG_DIR = IPlatformHelper.INSTANCE.getConfigDirectory().toFile();
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().serializeNulls().create();
    public static final String FILE_NAME = WhatDidIJustKill.MOD_ID + ".json";
    private static final File CONFIG_FILE = new File(CONFIG_DIR, FILE_NAME);

    public static boolean loadAndVerifyConfig() {
        ConfigManager.createConfigFolder();

        if (!CONFIG_FILE.exists()) {
            WhatDidIJustKillConfig.setToDefault();
            ConfigManager.writeConfigToFile();
            WhatDidIJustKill.LOGGER.warn("No config '{}' was found, created a new one.", FILE_NAME);
            return true;
        }

        AtomicBoolean successFul = new AtomicBoolean(true);
        try {
            ConfigManager.loadConfigFromFile(() -> successFul.set(false));
            if (successFul.get()) {
                WhatDidIJustKill.LOGGER.info("Config '{}' was successfully loaded.", FILE_NAME);
            } else {
                WhatDidIJustKill.LOGGER.error("Config '{}' did not load correctly. Using default config.", FILE_NAME);
                WhatDidIJustKillConfig.setToDefault();
            }
        } catch (Exception e) {
            WhatDidIJustKill.LOGGER.error(e.getMessage());
            WhatDidIJustKill.LOGGER.error("Error loading config '{}', config hasn't been loaded. Using default config.", FILE_NAME);
            WhatDidIJustKillConfig.setToDefault();
            successFul.set(false);
        }

        return successFul.get();
    }

    private static void loadConfigFromFile(Runnable setUnSuccessful) throws FileNotFoundException {
        JsonParser parser = new JsonParser();
        JsonElement jsonElement = parser.parse(new FileReader(CONFIG_FILE));
        JsonObject json = jsonElement.getAsJsonObject();
        WhatDidIJustKillConfig.fromJson(json, setUnSuccessful);
    }

    private static boolean writeConfigToFile() {
        try {
            JsonElement jsonObject = WhatDidIJustKillConfig.toJson();
            JsonWriter writer = new JsonWriter(new FileWriter(CONFIG_FILE));
            writer.setIndent("\t");
            GSON.toJson(jsonObject, writer);
            writer.close();
            return true;
        } catch (Exception e) {
            WhatDidIJustKill.LOGGER.error("There was an error writing the config to file: '{}'", FILE_NAME);
            WhatDidIJustKill.LOGGER.error(e.getMessage());
        }
        return false;
    }

    public static boolean resetConfig() {
        try {
            WhatDidIJustKillConfig.setToDefault();
            boolean success = ConfigManager.writeConfigToFile();
            WhatDidIJustKill.LOGGER.info("Config '{}' was set to default.", FILE_NAME);
            return success;
        } catch (Exception e) {
            WhatDidIJustKill.LOGGER.error("Error resetting config '{}'", FILE_NAME);
            WhatDidIJustKill.LOGGER.error(e.getMessage());
        }
        return false;
    }

    public static boolean reloadConfig() {
        try {
            return ConfigManager.loadAndVerifyConfig();
        } catch (Exception e) {
            WhatDidIJustKill.LOGGER.error("Error reloading config '{}'", FILE_NAME);
            WhatDidIJustKill.LOGGER.error(e.getMessage());
        }
        return false;
    }

    public static String getConfigPath() {
        return CONFIG_FILE.getAbsolutePath();
    }

    private static void createConfigFolder() {
        if (!CONFIG_DIR.exists()) {
            if (!CONFIG_DIR.mkdirs()) {
                throw new RuntimeException("Could not create config folder: " + CONFIG_DIR.getAbsolutePath());
            }
        }
    }
}
