package ru.hantu.ralp;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import java.util.UUID;

public class LoginCommand implements CommandExecutor {
    private final Main plugin;

    public LoginCommand(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            plugin.adventure().sender(sender).sendMessage(
                    plugin.getLocaleManager().getMessageComponent("errors.player-only")
            );
            return true;
        }

        UUID uuid = player.getUniqueId();

        if (!plugin.getAuthListener().isAuthenticated(player)) {
        } else {
            plugin.adventure().player(player).sendMessage(
                    plugin.getLocaleManager().getMessageComponent("login.already-logged-in")
            );
            return true;
        }

        if (args.length != 1) {
            plugin.adventure().player(player).sendMessage(
                    plugin.getLocaleManager().getMessageComponent("login.usage")
            );
            return true;
        }

        if (plugin.getAuthManager().isBlocked(uuid)) {
            long remaining = plugin.getAuthManager().getBlockedUntil(uuid) - System.currentTimeMillis();
            long seconds = (remaining + 999) / 1000;
            String message = plugin.getLocaleManager().getMessage("login.blocked-time")
                    .replace("{time}", String.valueOf(seconds));
            plugin.adventure().player(player).sendMessage(
                    plugin.getLocaleManager().getMiniMessage().deserialize(message)
            );
            return true;
        }

        // Проверяем пароль
        if (plugin.getPasswordManager().verifyPassword(player, args[0])) {
            plugin.getAuthManager().setAuthenticated(uuid, true);
            plugin.adventure().player(player).sendMessage(
                    plugin.getLocaleManager().getMessageComponent("login.success")
            );
            return true;
        } else {
            plugin.getAuthManager().recordFailedAttempt(uuid);
            if (plugin.getAuthManager().isBlocked(uuid)) {
                plugin.adventure().player(player).sendMessage(
                        plugin.getLocaleManager().getMessageComponent("login.blocked")
                );
            } else {
                plugin.adventure().player(player).sendMessage(
                        plugin.getLocaleManager().getMessageComponent("login.wrong-password")
                );
            }
            return false;
        }
    }
}