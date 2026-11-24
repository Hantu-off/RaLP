package ru.hantu.ralp;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class TabComplete implements TabCompleter {
    private final Main plugin;

    public TabComplete(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        var list = new ArrayList<String>();
        if (command.getName().equals("unreg")) {
            if (args.length == 1) {
                String partialName = args[0].toLowerCase();
                return Bukkit.getOnlinePlayers().stream()
                        .map(Player::getName)
                        .filter(name -> name.toLowerCase().startsWith(partialName))
                        .collect(Collectors.toList());
            }
            return new ArrayList<>();
        } else if (command.getName().equalsIgnoreCase("ralp") && sender.hasPermission("ralp.admin")) {
            if (args.length == 1) {
                Collections.addAll(list, "config", "reload");
            }
            else if (args.length == 2 && args[0].equalsIgnoreCase("config")) {
                Collections.addAll(list, "lang", "autologin", "min-password-length", "block-time", "max-attempts", "sound");
            }
            else if (args.length == 3 && args[0].equalsIgnoreCase("config") && args[1].equalsIgnoreCase("lang")) {
                Collections.addAll(list, "en_us", "ru_ru", "de_de", "es_es", "pt_pt", "fr_fr",
                        "tr_tr", "it_it", "ko_kr", "ja_jp", "zh_cn", "pl_pl");
            } else if (args.length == 3 && args[0].equalsIgnoreCase("config") && args[1].equalsIgnoreCase("autologin")
                   || (args.length == 4 && args[0].equalsIgnoreCase("config") && args[1].equalsIgnoreCase("sound") && args[2].equalsIgnoreCase("enabled"))) {
                Collections.addAll(list, "true", "false");
            } else if (args.length == 3 && args[0].equalsIgnoreCase("config") && args[1].equalsIgnoreCase("sound")) {
                Collections.addAll(list, "enabled", "name", "volume", "pitch");
            }
        }

        return new ArrayList<>(list);
    }
}
