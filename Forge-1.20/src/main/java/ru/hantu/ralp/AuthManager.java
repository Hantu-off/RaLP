package ru.hantu.ralp;

import net.minecraft.server.level.ServerPlayer;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AuthManager {
    private static final Map<UUID, Boolean> authenticated = new HashMap<>();
    private static final Map<UUID, Integer> attempts = new HashMap<>();
    private static final Map<UUID, Long> blockedUntil = new HashMap<>();

    public static boolean isAuthenticated(ServerPlayer player) {
        return authenticated.getOrDefault(player.getUUID(), false);
    }

    public static void setAuthenticated(ServerPlayer player, boolean auth) {
        authenticated.put(player.getUUID(), auth);
    }

    public static boolean isBlocked(ServerPlayer player) {
        Long until = blockedUntil.get(player.getUUID());
        if (until != null && System.currentTimeMillis() < until) {
            return true;
        }
        blockedUntil.remove(player.getUUID());
        attempts.remove(player.getUUID());
        return false;
    }

    public static void recordFailed(ServerPlayer player) {
        if (isBlocked(player)) return;
        UUID uuid = player.getUUID();
        int count = attempts.getOrDefault(uuid, 0) + 1;
        attempts.put(uuid, count);
        if (count >= ConfigManager.maxLoginAttempts) {
            blockedUntil.put(uuid, System.currentTimeMillis() + ConfigManager.blockTimeSeconds * 1000L);
            attempts.remove(uuid);
        }
    }

    public static long getRemainingSeconds(ServerPlayer player) {
        Long until = blockedUntil.get(player.getUUID());
        return until != null ? Math.max(0, (until - System.currentTimeMillis()) / 1000) : 0;
    }

    public static void clear(ServerPlayer player) {
        attempts.remove(player.getUUID());
        blockedUntil.remove(player.getUUID());
    }
}