package ru.hantu.ralp;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.configuration.file.YamlConfiguration;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class LocaleManager {
    private final Main plugin;
    private Map<String, Map<String, String>> messages;
    private String defaultLanguage;
    private final MiniMessage miniMessage;
    private final LegacyComponentSerializer legacySerializer;

    public LocaleManager(Main plugin) {
        this.plugin = plugin;
        this.messages = new HashMap<>();
        this.miniMessage = MiniMessage.miniMessage();
        this.legacySerializer = LegacyComponentSerializer.builder()
                .character('§')
                .hexColors()
                .useUnusualXRepeatedCharacterHexFormat()
                .build();
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
                    String message = config.getString(lang + "." + key);
                    if (message != null) {
                        // Заменяем старые цветовые коды на MiniMessage формат
                        message = message.replace("§", "<dark_gray>")
                                .replace("&", "");
                        langMessages.put(key, message);
                    }
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

    public Component getMessageComponent(String key) {
        String message = getMessage(key);
        try {
            return miniMessage.deserialize(message);
        } catch (Exception e) {
            plugin.getLogger().warning("Failed to parse message with key '" + key + "': " + e.getMessage());
            return Component.text(message);
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

