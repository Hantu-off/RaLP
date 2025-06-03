package net.hantu.ralp;

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
        if (!(sender instanceof Player)) {
            sender.sendMessage(plugin.getLocaleManager().getMessage("errors.player-only"));
            return true;
        }

        Player player = (Player) sender;

        if (args.length != 2) {
            player.sendMessage(plugin.getLocaleManager().getMessage("register.usage"));
            return true;
        }

        if (!args[0].equals(args[1])) {
            player.sendMessage(plugin.getLocaleManager().getMessage("register.passwords-not-match"));
            return true;
        }

        if (plugin.getPasswordManager().register(player, args[0])) {
            plugin.getAuthManager().setAuthenticated(player.getUniqueId(), true);
            plugin.getAuthListener().markAsAuthenticated(player);
            player.sendMessage(plugin.getLocaleManager().getMessage("register.success"));
            return true;
        }

        return false;
    }
}