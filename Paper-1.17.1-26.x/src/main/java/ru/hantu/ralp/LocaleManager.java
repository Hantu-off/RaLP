package ru.hantu.ralp;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class LocaleManager {
    private final Main plugin;
    private Map<String, Map<String, String>> messages;
    private String defaultLanguage;
    private final MiniMessage miniMessage;

    public LocaleManager(Main plugin) {
        this.plugin = plugin;
        this.messages = new HashMap<>();
        this.miniMessage = MiniMessage.miniMessage();
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

            addMissingMessages(messagesFile);

            YamlConfiguration config = YamlConfiguration.loadConfiguration(messagesFile);
            defaultLanguage = plugin.getConfig().getString("settings.default-language", "en_us");

            for (String lang : config.getKeys(false)) {
                Map<String, String> langMessages = new HashMap<>();
                if (config.getConfigurationSection(lang) != null) {
                    for (String key : config.getConfigurationSection(lang).getKeys(true)) {
                        String message = config.getString(lang + "." + key);
                        if (message != null) {
                            langMessages.put(key, message);
                        }
                    }
                }
                messages.put(lang, langMessages);
            }
        } catch (Exception e) {
            plugin.getLogger().severe("Failed to load messages: " + e.getMessage());
            Map<String, String> fallback = new HashMap<>();
            fallback.put("errors.player-only", "<red>This command is for players only!");
            fallback.put("login.blocked", "<red>You are temporarily blocked due to too many failed login attempts!");
            fallback.put("login.blocked-time", "<red>You are blocked for {time} more seconds!");
            fallback.put("login.already-logged-in", "<green>You are already logged in!");
            messages.put("en_us", fallback);
        }
    }

    private void addMissingMessages(File messagesFile) {
        InputStream resource = plugin.getResource("messages.yml");
        if (resource == null) {
            return;
        }

        try (InputStreamReader reader = new InputStreamReader(resource, StandardCharsets.UTF_8)) {
            YamlConfiguration defaults = YamlConfiguration.loadConfiguration(reader);
            YamlConfiguration current = YamlConfiguration.loadConfiguration(messagesFile);

            int added = 0;
            for (String language : defaults.getKeys(false)) {
                ConfigurationSection defaultSection = defaults.getConfigurationSection(language);
                if (defaultSection == null) {
                    continue;
                }

                for (String key : defaultSection.getKeys(true)) {
                    if (!defaultSection.isString(key)) {
                        continue;
                    }

                    String path = language + "." + key;
                    if (!current.contains(path)) {
                        current.set(path, defaultSection.getString(key));
                        added++;
                    }
                }
            }

            if (added > 0) {
                current.save(messagesFile);
                plugin.getLogger().info("Added " + added + " missing message key(s) to messages.yml");
            }
        } catch (IOException | RuntimeException e) {
            plugin.getLogger().warning("Failed to update messages.yml: " + e.getMessage());
        }
    }

    public MiniMessage getMiniMessage() {
        return miniMessage;
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

    public Component getMessageComponent(String key, Map<String, String> placeholders) {
        String message = getMessage(key);

        for (Map.Entry<String, String> entry : placeholders.entrySet()) {
            message = message.replace("{" + entry.getKey() + "}", entry.getValue());
        }

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
                langMessages.getOrDefault(key, "Message not found: " + key + " please delete message.yml from RaLP folder") :
                "Message not found: " + key;
    }

    public void reload() {
        loadMessages();
        this.defaultLanguage = plugin.getConfig().getString("settings.default-language", "en_us");
    }
}