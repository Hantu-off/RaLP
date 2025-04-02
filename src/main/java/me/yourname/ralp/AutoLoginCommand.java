package me.yourname.ralp;
import org.bukkit.configuration.file.FileConfiguration;
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

        if (args.length != 1 || (!args[0].equalsIgnoreCase("true") && !args[0].equalsIgnoreCase("false"))) {
            player.sendMessage(plugin.getLocaleManager().getMessage("autologin.usage"));
            return true;
        }

        boolean enable = Boolean.parseBoolean(args[0]);
        plugin.setAutoLogin(player.getUniqueId(), enable);

        if (enable) {
            player.sendMessage(plugin.getLocaleManager().getMessage("autologin.enabled"));
        } else {
            player.sendMessage(plugin.getLocaleManager().getMessage("autologin.disabled"));
        }

        return true;
    }
}