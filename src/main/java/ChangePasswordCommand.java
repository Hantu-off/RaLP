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
            sender.sendMessage(plugin.getLocaleManager().getMessage("errors.player-only"));
            return true;
        }

        Player player = (Player) sender;

        if (args.length != 3) {
            player.sendMessage(plugin.getLocaleManager().getMessage("changepassword.usage"));
            return true;
        }

        if (!plugin.getPasswordManager().isPlayerRegistered(player)) {
            player.sendMessage(plugin.getLocaleManager().getMessage("login.not-registered"));
            return true;
        }

        if (!args[1].equals(args[2])) {
            player.sendMessage(plugin.getLocaleManager().getMessage("register.passwords-not-match"));
            return true;
        }

        if (!plugin.getPasswordManager().verifyPassword(player, args[0])) {
            player.sendMessage(plugin.getLocaleManager().getMessage("login.wrong-password"));
            return true;
        }

        if (plugin.getPasswordManager().changePassword(player, args[1])) {
            plugin.getAuthManager().setAuthenticated(player.getUniqueId(), false);
            player.sendMessage(plugin.getLocaleManager().getMessage("changepassword.success"));
            return true;
        }

        player.sendMessage(plugin.getLocaleManager().getMessage("changepassword.error"));
        return false;
    }
}