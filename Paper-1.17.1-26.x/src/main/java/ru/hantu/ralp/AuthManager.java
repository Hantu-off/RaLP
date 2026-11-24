package ru.hantu.ralp;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import java.util.*;

public class AuthManager {
    private final Main plugin;
    private final Set<UUID> loggedInPlayers = new HashSet<>();
    private final Map<UUID, String> autoLoginData = new HashMap<>();
    private final Map<UUID, Long> sessions = new HashMap<>();

    private final Map<UUID, Integer> loginAttempts = new HashMap<>();
    private final Map<UUID, Long> blockedUntil = new HashMap<>();

    private final long sessionDuration;
    private final int maxLoginAttempts;
    private final long blockTimeMillis;

    public AuthManager(Main plugin) {
        this.plugin = plugin;
        this.sessionDuration = plugin.getConfig().getLong("settings.session-duration", 30) * 60 * 1000;
        this.maxLoginAttempts = plugin.getConfig().getInt("settings.max-login-attempts", 3);
        this.blockTimeMillis = plugin.getConfig().getLong("settings.block-time", 300) * 1000;
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

    public boolean isBlocked(UUID uuid) {
        Long blockedUntilTime = blockedUntil.get(uuid);
        return blockedUntilTime != null && System.currentTimeMillis() < blockedUntilTime;
    }

    public void recordFailedAttempt(UUID uuid) {
        if (isBlocked(uuid)) return;

        int attempts = loginAttempts.getOrDefault(uuid, 0) + 1;
        loginAttempts.put(uuid, attempts);

        if (attempts >= maxLoginAttempts) {
            blockedUntil.put(uuid, System.currentTimeMillis() + blockTimeMillis);
            loginAttempts.remove(uuid);
        }
    }

    public void clearAttempts(UUID uuid) {
        loginAttempts.remove(uuid);
        blockedUntil.remove(uuid);
    }

    public boolean tryAutoLogin(Player player) {
        if (!plugin.getConfig().getBoolean("settings.enable-auto-login", true)) {
            return false;
        }

        UUID uuid = player.getUniqueId();
        if (isBlocked(uuid)) {
            plugin.adventure().player(player).sendMessage(
                    plugin.getLocaleManager().getMessageComponent("login.blocked")
            );
            return false;
        }

        String currentIp = player.getAddress().getAddress().getHostAddress();
        String savedData = autoLoginData.get(uuid);

        if (savedData != null && savedData.equals(currentIp)) {
            setAuthenticated(uuid, true);
            clearAttempts(uuid);
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
            clearAttempts(playerId);
            plugin.getAuthListener().markAsAuthenticated(player);
        } else {
            sessions.remove(playerId);
            loggedInPlayers.remove(playerId);
            autoLoginData.remove(playerId);
            clearAttempts(playerId);
        }
    }

    public void logout(Player player) {
        UUID uuid = player.getUniqueId();
        setAuthenticated(uuid, false);
        sessions.remove(uuid);
        loggedInPlayers.remove(uuid);
        autoLoginData.remove(uuid);
        clearAttempts(uuid);
        saveAutoLoginData();
    }

    public long getBlockedUntil(UUID uuid) {
        return blockedUntil.getOrDefault(uuid, 0L);
    }

    public boolean hasActiveSession(UUID playerId) {
        Long expireTime = sessions.get(playerId);
        return expireTime != null && System.currentTimeMillis() < expireTime;
    }
}