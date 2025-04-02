package me.yourname.ralp;

import org.bukkit.command.*;
import org.bukkit.entity.Player;

public class LoginCommand implements CommandExecutor {
    private final Main plugin;

    public LoginCommand(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("\u00A7c\u0422\u043E\u043B\u044C\u043A\u043E \u0434\u043B\u044F \u0438\u0433\u0440\u043E\u043A\u043E\u0432!");
            return true;
        }

        Player player = (Player) sender;
        if (args.length != 1) {
            player.sendMessage("\u00A7c\u0418\u0441\u043F\u043E\u043B\u044C\u0437\u0443\u0439\u0442\u0435: /login <\u043F\u0430\u0440\u043E\u043B\u044C>");
            return true;
        }

        plugin.getPasswordManager().login(player, args[0]);
        return true;
    }
}