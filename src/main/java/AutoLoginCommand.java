package net.hantu.ralp;

import net.kyori.adventure.text.Component;
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
            plugin.adventure().sender(sender).sendMessage(
                    plugin.getLocaleManager().getMessageComponent("errors.player-only")
            );
            return true;
        }

        Player player = (Player) sender;

        if (args.length != 1) {
            plugin.adventure().player(player).sendMessage(
                    plugin.getLocaleManager().getMessageComponent("autologin.usage")
            );
            return true;
        }

        boolean enable;
        if (args[0].equalsIgnoreCase("on")) {
            enable = true;
            plugin.adventure().sender(sender).sendMessage(plugin.getLocaleManager().getMessageComponent("autologin.enabled"));
        } else if (args[0].equalsIgnoreCase("off")) {
            enable = false;
            plugin.adventure().sender(sender).sendMessage(plugin.getLocaleManager().getMessageComponent("autologin.disabled"));
        } else {
            plugin.adventure().player(player).sendMessage(
                    plugin.getLocaleManager().getMessageComponent("autologin.usage")
            );
            return true;
        }

        plugin.getAuthManager().setAutoLogin(player, enable);
        return true;
    }
}