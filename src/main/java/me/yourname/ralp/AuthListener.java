package me.yourname.ralp;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.UUID;

public class AuthListener implements Listener {
    private final Main plugin;

    public AuthListener(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();

        showAuthMessage(player);

        plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, () -> {
            if (!plugin.getAuthManager().isLoggedIn(uuid)) {
                showAuthMessage(player);
            }
        }, 0L, 100L); // Повторять каждые 5 секунд
    }

    private void showAuthMessage(Player player) {
        String message;
        if (!plugin.getPasswordManager().isRegistered(player)) {
            message = plugin.getLocaleManager().getMessage("register.usage");
        } else {
            message = plugin.getLocaleManager().getMessage("login.usage");
        }

        // Универсальный метод отправки сообщения
        sendActionBar(player, message);
    }

    private void sendActionBar(Player player, String message) {
        try {
            // Попытка использовать современный API (1.16+)
            Class<?> adventureClass = Class.forName("net.kyori.adventure.text.Component");
            Object component = Class.forName("net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer")
                    .getMethod("legacySection")
                    .invoke(null)
                    .getClass()
                    .getMethod("deserialize", String.class)
                    .invoke(null, message);

            player.getClass().getMethod("sendActionBar", adventureClass).invoke(player, component);
        } catch (Exception e) {
            // Fallback для старых версий
            player.sendTitle("", message, 10, 70, 20);
        }
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        if (!plugin.getAuthManager().isLoggedIn(event.getPlayer().getUniqueId())) {
            event.setTo(event.getFrom());
        }
    }
}