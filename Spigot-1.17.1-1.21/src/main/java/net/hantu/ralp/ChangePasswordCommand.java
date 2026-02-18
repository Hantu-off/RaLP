package net.hantu.ralp;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ChangePasswordCommand implements CommandExecutor {
    private final Main plugin;

    public ChangePasswordCommand(Main plugin) {
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

        if (args.length != 3) {
            plugin.adventure().player(player).sendMessage(
                    plugin.getLocaleManager().getMessageComponent("changepassword.usage")
            );
            return true;
        }

        if (!plugin.getPasswordManager().isPlayerRegistered(player)) {
            plugin.adventure().player(player).sendMessage(
                    plugin.getLocaleManager().getMessageComponent("login.not-registered")
            );
            return true;
        }

        if (!args[1].equals(args[2])) {
            plugin.adventure().player(player).sendMessage(
                    plugin.getLocaleManager().getMessageComponent("register.passwords-not-match")
            );
            return true;
        }

        if (!plugin.getPasswordManager().verifyPassword(player, args[0])) {
            plugin.adventure().player(player).sendMessage(
                    plugin.getLocaleManager().getMessageComponent("login.wrong-password")
            );
            return true;
        }

        if (plugin.getPasswordManager().changePassword(player, args[1])) {
            plugin.getAuthManager().setAuthenticated(player.getUniqueId(), false);
            plugin.adventure().player(player).sendMessage(
                    plugin.getLocaleManager().getMessageComponent("changepassword.success")
            );
            return true;
        }

        plugin.adventure().player(player).sendMessage(
                plugin.getLocaleManager().getMessageComponent("changepassword.error")
        );
        return false;
    }
}