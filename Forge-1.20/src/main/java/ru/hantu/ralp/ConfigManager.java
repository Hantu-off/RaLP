package ru.hantu.ralp;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

public class ConfigManager {
    public static final List<String> SUPPORTED_LANGUAGES = Arrays.asList(
            "en", "ru", "de", "es", "fr", "pt", "tr", "it", "ko", "ja", "zh", "pl"
    );

    public static int maxLoginAttempts = 3;
    public static int blockTimeSeconds = 300;
    public static int minPasswordLength = 4;
    public static String language = "en";

    private static final Path CONFIG_FILE = Paths.get("config", "ralp", "config.json");
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public static void init() {
        try {
            Files.createDirectories(CONFIG_FILE.getParent());
            if (Files.exists(CONFIG_FILE)) {
                try (Reader reader = Files.newBufferedReader(CONFIG_FILE)) {
                    ConfigData data = GSON.fromJson(reader, ConfigData.class);
                    if (data != null) {
                        maxLoginAttempts = data.maxLoginAttempts;
                        blockTimeSeconds = data.blockTimeSeconds;
                        minPasswordLength = data.minPasswordLength;
                        language = SUPPORTED_LANGUAGES.contains(data.language) ? data.language : "en";
                    }
                }
            } else {
                save();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void save() {
        try (Writer writer = Files.newBufferedWriter(CONFIG_FILE)) {
            ConfigData data = new ConfigData();
            data.maxLoginAttempts = maxLoginAttempts;
            data.blockTimeSeconds = blockTimeSeconds;
            data.minPasswordLength = minPasswordLength;
            data.language = language;
            GSON.toJson(data, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static class ConfigData {
        public int maxLoginAttempts = 3;
        public int blockTimeSeconds = 300;
        public int minPasswordLength = 4;
        public String language = "en";
    }
}