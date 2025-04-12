package net.hantu.ralp;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import java.util.UUID;

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
        UUID uuid = player.getUniqueId();

        if (args.length != 1 || (!args[0].equalsIgnoreCase("on") && !args[0].equalsIgnoreCase("off"))) {
            player.sendMessage(plugin.getLocaleManager().getMessage("autologin.usage"));
            return true;
        }

        boolean enable = args[0].equalsIgnoreCase("on");
        plugin.getAuthManager().setAutoLogin(uuid, enable ? player.getAddress().getAddress().getHostAddress() : null);

        String message = enable ? "autologin.enabled" : "autologin.disabled";
        player.sendMessage(plugin.getLocaleManager().getMessage(message));

        return true;
    }
}