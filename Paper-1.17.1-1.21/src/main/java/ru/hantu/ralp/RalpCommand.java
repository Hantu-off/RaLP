package ru.hantu.ralp;

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
        if (label.equalsIgnoreCase("ralp") && args.length == 1 && args[0].equals("[hidden]")) {
            return true;
        }
        if (args.length == 0 || !args[0].equalsIgnoreCase("reload")) {
            plugin.adventure().sender(sender).sendMessage(
                    plugin.getLocaleManager().getMessageComponent("ralp.usage")
            );
            return true;
        }

        if (!sender.hasPermission("ralp.admin")) {
            plugin.adventure().sender(sender).sendMessage(
                    plugin.getLocaleManager().getMessageComponent("errors.no-permission")
            );
            return true;
        }

        if (args.length > 0 && args[0].equalsIgnoreCase("silentcmd")) {
            return true; // Просто поглощаем команду
        }

        plugin.reloadConfig();
        plugin.getLocaleManager().reload();
        plugin.adventure().sender(sender).sendMessage(
                plugin.getLocaleManager().getMessageComponent("commands.reload-success")
        );
        return true;
    }
}