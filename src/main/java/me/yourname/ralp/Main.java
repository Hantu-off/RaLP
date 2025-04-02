package me.yourname.ralp;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import java.util.UUID;
public class Main extends JavaPlugin {
    private static Main instance;
    private LocaleManager localeManager;
    private PasswordManager passwordManager;
    private AuthManager authManager;

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();
        reloadConfig(); // Загружает актуальные значения
        saveResource("messages.yml", false);

        this.localeManager = new LocaleManager(this);
        this.passwordManager = new PasswordManager(this);
        this.authManager = new AuthManager(this);

        new CommandManager(this);
        getServer().getPluginManager().registerEvents(new AuthListener(this), this);
    }

    public static Main getInstance() { return instance; }
    public LocaleManager getLocaleManager() { return localeManager; }
    public PasswordManager getPasswordManager() { return passwordManager; }
    public AuthManager getAuthManager() { return authManager; }

    @Override
    public FileConfiguration getConfig() {
        return super.getConfig();
    }

    public void setAutoLogin(UUID playerId, boolean enabled) {
        authManager.setAutoLogin(playerId, enabled);
    }
    public boolean isModernVersion() {
        try {
            Class.forName("net.kyori.adventure.text.Component");
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }
}