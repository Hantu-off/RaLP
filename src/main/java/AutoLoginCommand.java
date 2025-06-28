package net.hantu.ralp;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AutoLoginCommand implements CommandExecutor {
    private final Main plugin;

    public AutoLoginCommand(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(plugin.getLocaleManager().getMessage("errors.player-only"));
            return true;
        }

        Player player = (Player) sender;

        if (args.length != 1) {
            player.sendMessage(plugin.getLocaleManager().getMessage("autologin.usage"));
            return true;
        }

        boolean enable;
        if (args[0].equalsIgnoreCase("on")) {
            enable = true;
        } else if (args[0].equalsIgnoreCase("off")) {
            enable = false;
        } else {
            player.sendMessage(plugin.getLocaleManager().getMessage("autologin.usage"));
            return true;
        }

        plugin.getAuthManager().setAutoLogin(player, enable);
        return true;
    }
}