package net.hantu.ralp;

import org.bukkit.entity.Player;
import org.bukkit.configuration.file.FileConfiguration;
import java.util.*;

public class AuthManager {
    private final Main plugin;
    private final Set<UUID> loggedInPlayers = new HashSet<>();
    private final Map<UUID, String> autoLoginIps = new HashMap<>();
    private final Map<UUID, Long> sessions = new HashMap<>();
    private final long sessionDuration;

    public AuthManager(Main plugin) {
        this.plugin = plugin;
        this.sessionDuration = plugin.getConfig().getLong("settings.session-duration", 30) * 60 * 1000;
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
        } else {
            sessions.remove(playerId);
            loggedInPlayers.remove(playerId);
        }
    }

    public void setAutoLogin(UUID playerId, String ip) {
        if (ip != null) {
            autoLoginIps.put(playerId, ip);
        } else {
            autoLoginIps.remove(playerId);
        }
    }

    public boolean tryAutoLogin(UUID playerId, String currentIp) {
        String savedIp = autoLoginIps.get(playerId);
        if (savedIp != null && savedIp.equals(currentIp)) {
            setAuthenticated(playerId, true);
            return true;
        }
        return false;
    }
}