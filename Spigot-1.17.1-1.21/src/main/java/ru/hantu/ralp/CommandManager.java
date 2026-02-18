package ru.hantu.ralp;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import java.util.ArrayList;
import java.util.List;

public class CommandManager implements TabCompleter {
    private final Main plugin;

    public CommandManager(Main plugin) {
        this.plugin = plugin;
        plugin.getCommand("register").setExecutor(new RegisterCommand(plugin));
        plugin.getCommand("login").setExecutor(new LoginCommand(plugin));
        plugin.getCommand("l").setExecutor(new LoginCommand(plugin));
        plugin.getCommand("autologin").setExecutor(new AutoLoginCommand(plugin));
        plugin.getCommand("changepassword").setExecutor(new ChangePasswordCommand(plugin));
        plugin.getCommand("ralp").setExecutor(new RalpCommand(plugin));
        plugin.getCommand("ralp").setTabCompleter(this);

        UnregCommand unregCommand = new UnregCommand(plugin);
        plugin.getCommand("unreg").setExecutor(unregCommand);
        plugin.getCommand("unreg").setTabCompleter(unregCommand);

        plugin.getServer().getPluginManager().registerEvents(plugin.getAuthListener(), plugin);
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        List<String> completions = new ArrayList<>();
        if (cmd.getName().equalsIgnoreCase("ralp") && args.length == 1 && sender.hasPermission("ralp.admin")) {
            completions.add("reload");
        }
        if (!args[args.length - 1].isEmpty()) {
            completions.removeIf(s -> !s.toLowerCase().startsWith(args[args.length - 1].toLowerCase()));
        }
        return completions;
    }
}