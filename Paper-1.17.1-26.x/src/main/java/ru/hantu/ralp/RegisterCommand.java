package ru.hantu.ralp;

import net.kyori.adventure.text.Component;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RegisterCommand implements CommandExecutor {
    private final Main plugin;

    public RegisterCommand(Main plugin) {
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

        if (plugin.getPasswordManager().isPlayerRegistered(player)) {
            plugin.adventure().player(player).sendMessage(
                    plugin.getLocaleManager().getMessageComponent("register.already-registered")
            );
            return true;
        }

        if (args.length != 2) {
            plugin.adventure().player(player).sendMessage(
                    plugin.getLocaleManager().getMessageComponent("register.usage")
            );
            return true;
        }

        if (!args[0].equals(args[1])) {
            plugin.adventure().player(player).sendMessage(
                    plugin.getLocaleManager().getMessageComponent("register.passwords-not-match")
            );
            return true;
        }

        if (plugin.getPasswordManager().register(player, args[0])) {
            plugin.getAuthManager().setAuthenticated(player.getUniqueId(), true);
            plugin.getAuthListener().markAsAuthenticated(player);
            plugin.adventure().player(player).sendMessage(
                    plugin.getLocaleManager().getMessageComponent("register.success")
            );
            return true;
        }

        return false;
    }
}