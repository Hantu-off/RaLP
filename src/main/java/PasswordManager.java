package net.hantu.ralp;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.mindrot.jbcrypt.BCrypt;
import java.util.UUID;

public class PasswordManager {
    private final Main plugin;
    private final FileConfiguration config;

    public PasswordManager(Main plugin) {
        this.plugin = plugin;
        this.config = plugin.getConfig();
    }

    public boolean isPlayerRegistered(Player player) {
        return config.contains("players." + player.getUniqueId() + ".password");
    }

    public boolean hasPassword(UUID playerId) {
        return config.contains("players." + playerId + ".password");
    }

    public boolean verifyPassword(Player player, String password) {
        String hash = config.getString("players." + player.getUniqueId() + ".password");
        return hash != null && BCrypt.checkpw(password, hash);
    }

    public boolean processLogin(Player player, String password) {
        if (verifyPassword(player, password)) {
            plugin.getAuthManager().setAuthenticated(player.getUniqueId(), true);
            return true;
        }
        return false;
    }

    public boolean register(Player player, String password) {
        try {
            UUID uuid = player.getUniqueId();
            if (isPlayerRegistered(player)) {
                player.sendMessage(plugin.getLocaleManager().getMessage("register.already-exists"));
                return false;
            }

            String hash = BCrypt.hashpw(password, BCrypt.gensalt());
            config.set("players." + uuid + ".password", hash);
            plugin.saveConfig();
            return true;
        } catch (Exception e) {
            plugin.getLogger().severe("Registration error: " + e.getMessage());
            player.sendMessage("§cОшибка регистрации. Попробуйте снова.");
            return false;
        }
    }

    public boolean changePassword(Player player, String newPassword) {
        try {
            UUID uuid = player.getUniqueId();
            String hash = BCrypt.hashpw(newPassword, BCrypt.gensalt());
            config.set("players." + uuid + ".password", hash);
            plugin.saveConfig();
            return true;
        } catch (Exception e) {
            plugin.getLogger().severe("Password change error: " + e.getMessage());
            return false;
        }
    }
}