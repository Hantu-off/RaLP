package me.yourname.ralp;

import java.util.*;

public class AuthManager {
    private final Main plugin;
    private final Set<UUID> loggedInPlayers = new HashSet<>();
    private final Map<UUID, Boolean> autoLoginPlayers = new HashMap<>();
    private final Map<UUID, Long> sessions = new HashMap<>();
    private final long sessionDuration;

    public AuthManager(Main plugin) {
        this.plugin = plugin;
        this.sessionDuration = plugin.getConfig().getLong("settings.session-duration", 30) * 60 * 1000;
    }

    public boolean isLoggedIn(UUID playerId) {
        if (sessions.containsKey(playerId)) {
            if (System.currentTimeMillis() < sessions.get(playerId)) {
                return true;
            }
            sessions.remove(playerId);
        }
        return loggedInPlayers.contains(playerId);
    }

    public void setAuthenticated(UUID playerId, boolean status) {
        if (status) {
            sessions.put(playerId, System.currentTimeMillis() + sessionDuration);
            loggedInPlayers.add(playerId);
        } else {
            sessions.remove(playerId);
            loggedInPlayers.remove(playerId);
        }
    }

    public void setAutoLogin(UUID playerId, boolean enabled) {
        autoLoginPlayers.put(playerId, enabled);
    }

    public boolean hasAutoLogin(UUID playerId) {
        return autoLoginPlayers.getOrDefault(playerId, false);
    }
}