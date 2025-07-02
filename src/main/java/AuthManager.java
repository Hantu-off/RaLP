package net.hantu.ralp;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import java.util.*;

public class AuthManager {
    private final Main plugin;
    private final Set<UUID> loggedInPlayers = new HashSet<>();
    private final Map<UUID, String> autoLoginData = new HashMap<>();
    private final Map<UUID, Long> sessions = new HashMap<>();
    private final long sessionDuration;

    public AuthManager(Main plugin) {
        this.plugin = plugin;
        this.sessionDuration = plugin.getConfig().getLong("settings.session-duration", 30) * 60 * 1000;
        loadAutoLoginData();
    }

    private void loadAutoLoginData() {
        ConfigurationSection section = plugin.getConfig().getConfigurationSection("auto-login");
        if (section != null) {
            for (String key : section.getKeys(false)) {
                try {
                    UUID uuid = UUID.fromString(key);
                    autoLoginData.put(uuid, section.getString(key));
                } catch (IllegalArgumentException e) {
                    plugin.getLogger().warning("Invalid UUID in auto-login data: " + key);
                }
            }
        }
    }

    public void saveAutoLoginData() {
        for (Map.Entry<UUID, String> entry : autoLoginData.entrySet()) {
            plugin.getConfig().set("auto-login." + entry.getKey().toString(), entry.getValue());
        }
        plugin.saveConfig();
    }

    public boolean tryAutoLogin(Player player) {
        if (!plugin.getConfig().getBoolean("settings.enable-auto-login", true)) {
            return false;
        }

        UUID uuid = player.getUniqueId();
        String currentIp = player.getAddress().getAddress().getHostAddress();
        String savedData = autoLoginData.get(uuid);

        if (savedData != null && savedData.equals(currentIp)) {
            setAuthenticated(uuid, true);
            plugin.adventure().player(player).sendMessage(
                    plugin.getLocaleManager().getMessageComponent("autologin.success")
            );
            plugin.getLogger().info(player.getName() + " auto-logged in from " + currentIp);
            return true;
        }
        return false;
    }

    public void setAutoLogin(Player player, boolean enable) {
        UUID uuid = player.getUniqueId();
        if (enable) {
            if (!isLoggedIn(uuid)) {
                player.sendMessage(plugin.getLocaleManager().getMessage("errors.not-logged-in"));
                return;
            }
            String ip = player.getAddress().getAddress().getHostAddress();
            autoLoginData.put(uuid, ip);
        } else {
            autoLoginData.remove(uuid);
        }
        saveAutoLoginData();
    }

    public boolean isLoggedIn(UUID playerId) {
        return loggedInPlayers.contains(playerId) ||
                (sessions.containsKey(playerId) && System.currentTimeMillis() < sessions.get(playerId));
    }

    public void setAuthenticated(UUID playerId, boolean status) {
        Player player = plugin.getServer().getPlayer(playerId);
        if (player == null) return;

        if (status) {
            sessions.put(playerId, System.currentTimeMillis() + sessionDuration);
            loggedInPlayers.add(playerId);
            plugin.getAuthListener().markAsAuthenticated(player);
            // Не отправляем сообщение здесь, чтобы избежать дублирования
        } else {
            sessions.remove(playerId);
            loggedInPlayers.remove(playerId);
            autoLoginData.remove(playerId);
        }
    }

    public void logout(Player player) {
        UUID uuid = player.getUniqueId();
        setAuthenticated(uuid, false);
        sessions.remove(uuid);
        loggedInPlayers.remove(uuid);
        autoLoginData.remove(uuid);
        saveAutoLoginData();
    }
}