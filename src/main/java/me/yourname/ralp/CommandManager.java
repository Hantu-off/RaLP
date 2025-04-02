package me.yourname.ralp;

public class CommandManager {
    public CommandManager(Main plugin) {
        plugin.getCommand("register").setExecutor(new RegisterCommand(plugin));
        plugin.getCommand("r").setExecutor(new RegisterCommand(plugin));
        plugin.getCommand("login").setExecutor(new LoginCommand(plugin));
        plugin.getCommand("l").setExecutor(new LoginCommand(plugin));
        plugin.getCommand("autologin").setExecutor(new AutoLoginCommand(plugin));
        plugin.getCommand("ralp").setExecutor(new RalpCommand(plugin)); // Добавляем новую команду
    }
}