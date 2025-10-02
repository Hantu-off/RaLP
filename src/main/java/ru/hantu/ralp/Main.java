package ru.hantu.ralp;

import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
    private static Main instance;
    private AuthListener authListener;
    private LocaleManager localeManager;
    private PasswordManager passwordManager;
    private AuthManager authManager;
    private BukkitAudiences adventure;

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();
        saveResource("messages.yml", false);

        this.adventure = BukkitAudiences.create(this);
        this.authListener = new AuthListener(this);
        this.localeManager = new LocaleManager(this);
        this.passwordManager = new PasswordManager(this);
        this.authManager = new AuthManager(this);

        new CommandManager(this);
        getServer().getPluginManager().registerEvents(authListener, this);
    }

    @Override
    public void onDisable() {
        if (this.adventure != null) {
            this.adventure.close();
            this.adventure = null;
        }
    }

    public static Main getInstance() { return instance; }
    public AuthListener getAuthListener() { return authListener; }
    public LocaleManager getLocaleManager() { return localeManager; }
    public PasswordManager getPasswordManager() { return passwordManager; }
    public AuthManager getAuthManager() { return authManager; }
    public BukkitAudiences adventure() { return adventure; }
}