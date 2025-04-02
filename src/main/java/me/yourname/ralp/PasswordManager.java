package me.yourname.ralp;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.mindrot.jbcrypt.BCrypt;
import java.util.*;
public class PasswordManager {
    // Добавляем ограничение попыток входа
    private final Map<UUID, Integer> loginAttempts = new HashMap<>();
    private final Map<UUID, Long> blockedPlayers = new HashMap<>();
    private final Main plugin;
    private final FileConfiguration config;
    public boolean login(Player player, String password) {
        UUID uuid = player.getUniqueId();

        // Проверка на блокировку
        if (isBlocked(uuid)) {
            long remainingTime = (blockedPlayers.get(uuid) - System.currentTimeMillis()) / 1000;
            player.sendMessage(plugin.getLocaleManager().getMessage("login.blocked")
                    .replace("{time}", String.valueOf(remainingTime)));
            return false;
        }

        if (!isRegistered(player)) {
            player.sendMessage(plugin.getLocaleManager().getMessage("login.not-registered"));
            return false;
        }

        String storedHash = config.getString("players." + uuid + ".password");
        if (BCrypt.checkpw(password, storedHash)) {
            plugin.getAuthManager().setAuthenticated(uuid, true);
            loginAttempts.remove(uuid);
            player.sendMessage(plugin.getLocaleManager().getMessage("login.success"));
            return true;
        } else {
            int attempts = loginAttempts.getOrDefault(uuid, 0) + 1;
            loginAttempts.put(uuid, attempts);

            int maxAttempts = plugin.getConfig().getInt("settings.max-login-attempts", 3);
            if (attempts >= maxAttempts) {
                long blockTime = plugin.getConfig().getLong("settings.block-time-after-fail", 300) * 1000;
                blockedPlayers.put(uuid, System.currentTimeMillis() + blockTime);
                player.sendMessage(plugin.getLocaleManager().getMessage("login.blocked")
                        .replace("{time}", String.valueOf(blockTime / 1000)));
            } else {
                player.sendMessage(plugin.getLocaleManager().getMessage("login.wrong-password"));
                player.sendMessage(plugin.getLocaleManager().getMessage("login.attempts-left")
                        .replace("{attempts}", String.valueOf(maxAttempts - attempts)));
            }
            return false;
        }
    }
    public PasswordManager(Main plugin) {
        this.plugin = plugin;
        this.config = plugin.getConfig();
    }
    private boolean isBlocked(UUID uuid) {
        if (blockedPlayers.containsKey(uuid)) {
            if (System.currentTimeMillis() < blockedPlayers.get(uuid)) {
                return true;
            } else {
                blockedPlayers.remove(uuid);
                return false;
            }
        }
        return false;
    }
    public boolean tryAutoLogin(UUID uuid, String ip) {
        // Реализуйте логику автоматического входа позже
        return false;
    }
    public boolean isRegistered(Player player) {
        return config.contains("players." + player.getUniqueId() + ".password");
    }
    public boolean register(Player player, String password) {
        UUID uuid = player.getUniqueId();
        if (config.contains("players." + uuid + ".password")) {
            player.sendMessage(plugin.getLocaleManager().getMessage("register.already-exists"));
            return false;
        }
        String hash = BCrypt.hashpw(password, BCrypt.gensalt());
        config.set("players." + uuid + ".password", hash);
        plugin.saveConfig();
        return true;
    }
}