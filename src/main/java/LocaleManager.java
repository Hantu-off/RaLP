package net.hantu.ralp;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class LocaleManager {
    private final Main plugin;
    private Map<String, Map<String, String>> messages;
    private String defaultLanguage;

    public LocaleManager(Main plugin) {
        this.plugin = plugin;
        this.messages = new HashMap<>();
        loadMessages();
        this.defaultLanguage = plugin.getConfig().getString("settings.default-language", "en_us");
        plugin.getLogger().info("Selected language: " + defaultLanguage);
    }

    private void loadMessages() {
        try {
            File messagesFile = new File(plugin.getDataFolder(), "messages.yml");
            if (!messagesFile.exists()) {
                plugin.saveResource("messages.yml", false);
            }

            YamlConfiguration config = YamlConfiguration.loadConfiguration(messagesFile);
            defaultLanguage = plugin.getConfig().getString("settings.default-language", "en_us");

            for (String lang : config.getKeys(false)) {
                Map<String, String> langMessages = new HashMap<>();
                for (String key : config.getConfigurationSection(lang).getKeys(true)) {
                    langMessages.put(key, config.getString(lang + "." + key));
                }
                messages.put(lang, langMessages);
            }
        } catch (Exception e) {
            plugin.getLogger().severe("Failed to load messages: " + e.getMessage());
            Map<String, String> fallback = new HashMap<>();
            fallback.put("errors.player-only", "This command is for players only!");
            messages.put("en_us", fallback);
        }
    }

    public String getMessage(String key) {
        Map<String, String> langMessages = messages.getOrDefault(defaultLanguage, messages.get("en_us"));
        return langMessages != null ?
                langMessages.getOrDefault(key, key) :
                "Message not found: " + key;
    }

    public void reload() {
        loadMessages();
    }
}