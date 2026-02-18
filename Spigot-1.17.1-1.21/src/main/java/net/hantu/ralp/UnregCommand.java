package net.hantu.ralp;

import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class UnregCommand implements CommandExecutor, TabCompleter {
    private final Main plugin;

    public UnregCommand(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length != 1) {
            plugin.adventure().sender(sender).sendMessage(
                    plugin.getLocaleManager().getMessageComponent("unreg.usage")
            );
            return true;
        }

        String targetPlayerName = args[0];
        Player targetPlayer = Bukkit.getPlayer(targetPlayerName);

        // Проверяем, существует ли игрок в базе данных
        if (!plugin.getPasswordManager().isPlayerRegistered(targetPlayerName)) {
            plugin.adventure().sender(sender).sendMessage(
                    plugin.getLocaleManager().getMessageComponent("unreg.not-registered")
            );
            return true;
        }

        // Если игрок онлайн, выкидываем его из системы
        if (targetPlayer != null && targetPlayer.isOnline()) {
            plugin.getAuthManager().setAuthenticated(targetPlayer.getUniqueId(), false);
            plugin.getAuthListener().markAsAuthenticated(targetPlayer);
        }

        // Удаляем данные игрока
        plugin.getPasswordManager().unregisterPlayer(targetPlayerName);

        // Отправляем сообщение об успехе
        String successMessage = plugin.getLocaleManager().getMessage("unreg.success").replace("{player}", targetPlayerName);
        plugin.adventure().sender(sender).sendMessage(
                MiniMessage.miniMessage().deserialize(successMessage)
        );

        // Если игрок онлайн, отправляем ему сообщение
        if (targetPlayer != null && targetPlayer.isOnline()) {
            plugin.adventure().player(targetPlayer).sendMessage(
                    plugin.getLocaleManager().getMessageComponent("unreg.player-notify")
            );
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            String partialName = args[0].toLowerCase();
            return Bukkit.getOnlinePlayers().stream()
                    .map(Player::getName)
                    .filter(name -> name.toLowerCase().startsWith(partialName))
                    .collect(Collectors.toList());
        }
        return new ArrayList<>();
    }
}