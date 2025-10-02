package ru.hantu.ralp;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class LoginCommand implements CommandExecutor {
    private final Main plugin;

    public LoginCommand(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            plugin.adventure().sender(sender).sendMessage(
                    plugin.getLocaleManager().getMessageComponent("errors.player-only")
            );
            return true;
        }

        Player player = (Player) sender;

        if (args.length != 1) {
            plugin.adventure().player(player).sendMessage(
                    plugin.getLocaleManager().getMessageComponent("login.usage")
            );
            return true;
        }

        if (plugin.getPasswordManager().processLogin(player, args[0])) {
            plugin.getAuthListener().markAsAuthenticated(player);
            plugin.adventure().player(player).sendMessage(
                    plugin.getLocaleManager().getMessageComponent("login.success")
            );
            return true;
        }

        plugin.adventure().player(player).sendMessage(
                plugin.getLocaleManager().getMessageComponent("login.wrong-password")
        );
        return false;
    }
}