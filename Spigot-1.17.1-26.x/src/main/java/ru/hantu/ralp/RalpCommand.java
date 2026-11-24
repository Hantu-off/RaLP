package ru.hantu.ralp;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RalpCommand implements CommandExecutor {
    private final Main plugin;

    private static final List<String> SUPPORTED_LANGUAGES = Arrays.asList(
            "en_us", "ru_ru", "de_de", "es_es", "pt_pt", "fr_fr",
            "tr_tr", "it_it", "ko_kr", "ja_jp", "zh_cn", "pl_pl"
    );

    public RalpCommand(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!sender.hasPermission("ralp.admin")) {
            plugin.adventure().sender(sender).sendMessage(
                    plugin.getLocaleManager().getMessageComponent("errors.no-permission")
            );
            return true;
        }

        if (args.length == 0) {
            plugin.adventure().sender(sender).sendMessage(
                    plugin.getLocaleManager().getMessageComponent("ralp.usage")
            );
            return true;
        }

        if (args[0].equalsIgnoreCase("reload")) {
            plugin.reloadConfig();
            plugin.getLocaleManager().reload();
            plugin.adventure().sender(sender).sendMessage(
                    plugin.getLocaleManager().getMessageComponent("commands.reload-success")
            );
            return true;
        }

        if (args[0].equalsIgnoreCase("config")) {
            handleConfigCommand(sender, args);
            return true;
        }

        plugin.adventure().sender(sender).sendMessage(
                plugin.getLocaleManager().getMessageComponent("ralp.usage")
        );
        return true;
    }

    private void handleConfigCommand(CommandSender sender, String[] args) {
        if (args.length == 3) {
            String param = args[1].toLowerCase();
            String value = args[2];

            switch (param) {
                case "lang":
                    if (!SUPPORTED_LANGUAGES.contains(value.toLowerCase())) {
                        Map<String, String> placeholders = new HashMap<>();
                        placeholders.put("language", value);
                        placeholders.put("available", String.join(", ", SUPPORTED_LANGUAGES));
                        plugin.adventure().sender(sender).sendMessage(
                                plugin.getLocaleManager().getMessageComponent("config.unknown-language", placeholders)
                        );
                        return;
                    }
                    plugin.getConfig().set("settings.default-language", value.toLowerCase());
                    plugin.saveConfig();
                    plugin.getLocaleManager().reload();

                    Map<String, String> placeholdersLang = new HashMap<>();
                    placeholdersLang.put("language", value.toLowerCase());
                    plugin.adventure().sender(sender).sendMessage(
                            plugin.getLocaleManager().getMessageComponent("config.language-changed", placeholdersLang)
                    );
                    break;

                case "autologin":
                    Boolean autoLogin = parseBoolean(value);
                    if (autoLogin == null) {
                        plugin.adventure().sender(sender).sendMessage(
                                plugin.getLocaleManager().getMessageComponent("config.invalid-boolean")
                        );
                        return;
                    }
                    plugin.getConfig().set("settings.enable-auto-login", autoLogin);
                    plugin.saveConfig();
                    plugin.adventure().sender(sender).sendMessage(
                            plugin.getLocaleManager().getMessageComponent(
                                    autoLogin ? "config.autologin-enabled" : "config.autologin-disabled"
                            )
                    );
                    break;

                case "min-password-length":
                    Integer minLen = parseInt(value, 1, 64);
                    if (minLen == null) {
                        sendInvalidRangeError(sender, 1, 64);
                        return;
                    }
                    plugin.getConfig().set("settings.min-password-length", minLen);
                    plugin.saveConfig();

                    Map<String, String> placeholdersMin = new HashMap<>();
                    placeholdersMin.put("value", String.valueOf(minLen));
                    plugin.adventure().sender(sender).sendMessage(
                            plugin.getLocaleManager().getMessageComponent("config.min-password-changed", placeholdersMin)
                    );
                    break;

                case "block-time":
                    Integer blockTime = parseInt(value, 1, 86400);
                    if (blockTime == null) {
                        sendInvalidRangeError(sender, 1, 86400);
                        return;
                    }
                    plugin.getConfig().set("settings.block-time", blockTime);
                    plugin.saveConfig();

                    Map<String, String> placeholdersBlock = new HashMap<>();
                    placeholdersBlock.put("value", String.valueOf(blockTime));
                    plugin.adventure().sender(sender).sendMessage(
                            plugin.getLocaleManager().getMessageComponent("config.block-time-changed", placeholdersBlock)
                    );
                    break;

                case "max-attempts":
                    Integer maxAttempts = parseInt(value, 1, 100);
                    if (maxAttempts == null) {
                        sendInvalidRangeError(sender, 1, 100);
                        return;
                    }
                    plugin.getConfig().set("settings.max-login-attempts", maxAttempts);
                    plugin.saveConfig();

                    Map<String, String> placeholdersMax = new HashMap<>();
                    placeholdersMax.put("value", String.valueOf(maxAttempts));
                    plugin.adventure().sender(sender).sendMessage(
                            plugin.getLocaleManager().getMessageComponent("config.max-attempts-changed", placeholdersMax)
                    );
                    break;

                default:
                    plugin.adventure().sender(sender).sendMessage(
                            plugin.getLocaleManager().getMessageComponent("config.usage")
                    );
                    break;
            }
            return;
        }

        if (args.length == 4 && args[1].equalsIgnoreCase("sound")) {
            String param = args[2].toLowerCase();
            String value = args[3];

            switch (param) {
                case "enabled":
                    Boolean enabled = parseBoolean(value);
                    if (enabled == null) {
                        plugin.adventure().sender(sender).sendMessage(
                                plugin.getLocaleManager().getMessageComponent("config.invalid-boolean")
                        );
                        return;
                    }
                    plugin.getConfig().set("settings.sound.enabled", enabled);
                    plugin.saveConfig();
                    plugin.adventure().sender(sender).sendMessage(
                            plugin.getLocaleManager().getMessageComponent(
                                    enabled ? "config.sound-enabled" : "config.sound-disabled"
                            )
                    );
                    break;

                case "name":
                    plugin.getConfig().set("settings.sound.name", value);
                    plugin.saveConfig();
                    Map<String, String> placeholdersName = new HashMap<>();
                    placeholdersName.put("value", value);
                    plugin.adventure().sender(sender).sendMessage(
                            plugin.getLocaleManager().getMessageComponent("config.sound-name-changed", placeholdersName)
                    );
                    break;

                case "volume":
                    Double volume = parseDouble(value, 0.0, 10.0);
                    if (volume == null) {
                        sendInvalidRangeError(sender, 0.0, 10.0);
                        return;
                    }
                    plugin.getConfig().set("settings.sound.volume", volume);
                    plugin.saveConfig();
                    Map<String, String> placeholdersVol = new HashMap<>();
                    placeholdersVol.put("value", String.valueOf(volume));
                    plugin.adventure().sender(sender).sendMessage(
                            plugin.getLocaleManager().getMessageComponent("config.sound-volume-changed", placeholdersVol)
                    );
                    break;

                case "pitch":
                    Double pitch = parseDouble(value, 0.0, 2.0);
                    if (pitch == null) {
                        sendInvalidRangeError(sender, 0.0, 2.0);
                        return;
                    }
                    plugin.getConfig().set("settings.sound.pitch", pitch);
                    plugin.saveConfig();
                    Map<String, String> placeholdersPitch = new HashMap<>();
                    placeholdersPitch.put("value", String.valueOf(pitch));
                    plugin.adventure().sender(sender).sendMessage(
                            plugin.getLocaleManager().getMessageComponent("config.sound-pitch-changed", placeholdersPitch)
                    );
                    break;

                default:
                    plugin.adventure().sender(sender).sendMessage(
                            plugin.getLocaleManager().getMessageComponent("config.usage")
                    );
                    break;
            }
            return;
        }

        plugin.adventure().sender(sender).sendMessage(
                plugin.getLocaleManager().getMessageComponent("config.usage")
        );
    }

    private void sendInvalidRangeError(CommandSender sender, Object min, Object max) {
        Map<String, String> placeholders = new HashMap<>();
        placeholders.put("min", String.valueOf(min));
        placeholders.put("max", String.valueOf(max));
        plugin.adventure().sender(sender).sendMessage(
                plugin.getLocaleManager().getMessageComponent("config.invalid-number-range", placeholders)
        );
    }

    private Boolean parseBoolean(String value) {
        if (value.equalsIgnoreCase("true")) return true;
        if (value.equalsIgnoreCase("false")) return false;
        return null;
    }

    private Integer parseInt(String value, int min, int max) {
        try {
            int parsed = Integer.parseInt(value);
            if (parsed < min || parsed > max) return null;
            return parsed;
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private Double parseDouble(String value, double min, double max) {
        try {
            double parsed = Double.parseDouble(value.replace(",", "."));
            if (parsed < min || parsed > max) return null;
            return parsed;
        } catch (NumberFormatException e) {
            return null;
        }
    }
}