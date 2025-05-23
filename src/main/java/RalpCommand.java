package net.hantu.ralp;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class RalpCommand implements CommandExecutor {
    private final Main plugin;

    public RalpCommand(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length == 0 || !args[0].equalsIgnoreCase("reload")) {
            sender.sendMessage(plugin.getLocaleManager().getMessage("ralp.usage"));
            return true;
        }

        if (!sender.hasPermission("ralp.admin")) {
            sender.sendMessage(plugin.getLocaleManager().getMessage("errors.no-permission"));
            return true;
        }
        if (args.length > 0 && args[0].equalsIgnoreCase("silentcmd")) {
            return true; // Просто поглощаем команду
        }

        plugin.reloadConfig();
        plugin.getLocaleManager().reload();
        sender.sendMessage(plugin.getLocaleManager().getMessage("commands.reload-success"));
        return true;
    }
}