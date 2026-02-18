package ru.hantu.ralp;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import net.minecraft.server.level.ServerPlayer;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerDataStorage {
    private static final Path CONFIG_DIR = Paths.get("config", "ralp");
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Type MAP_TYPE = new TypeToken<Map<UUID, PlayerData>>(){}.getType();
    private static Map<UUID, PlayerData> data = new HashMap<>();

    public static void init() {
        try {
            Files.createDirectories(CONFIG_DIR);
            load();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void load() {
        Path file = CONFIG_DIR.resolve("players.json");
        if (Files.exists(file)) {
            try (Reader reader = Files.newBufferedReader(file)) {
                data = GSON.fromJson(reader, MAP_TYPE);
                if (data == null) data = new HashMap<>();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void save() {
        Path file = CONFIG_DIR.resolve("players.json");
        try (Writer writer = Files.newBufferedWriter(file)) {
            GSON.toJson(data, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean isRegistered(UUID uuid) {
        return data.containsKey(uuid);
    }

    public static void register(ServerPlayer player, String hash) {
        data.put(player.getUUID(), new PlayerData(hash, player.getGameProfile().getName()));
        save();
    }

    public static String getPasswordHash(UUID uuid) {
        PlayerData d = data.get(uuid);
        return d != null ? d.passwordHash : null;
    }

    public static void updatePassword(ServerPlayer player, String newHash) {
        PlayerData data = PlayerDataStorage.data.get(player.getUUID());
        if (data != null) {
            data.passwordHash = newHash;
            save();
        }
    }

    public static void unregister(UUID uuid) {
        data.remove(uuid);
        save();
    }

    public static class PlayerData {
        public String passwordHash;
        public String name;

        public PlayerData(String passwordHash, String name) {
            this.passwordHash = passwordHash;
            this.name = name;
        }
    }
}