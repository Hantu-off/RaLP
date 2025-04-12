package net.hantu.ralp;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.entity.Player;
import java.util.UUID;

public class Main extends JavaPlugin {
    private static Main instance;
    private AuthListener authListener;
    private LocaleManager localeManager;
    private PasswordManager passwordManager;
    private AuthManager authManager;

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();
        saveResource("messages.yml", false);

        this.authListener = new AuthListener(this);
        this.localeManager = new LocaleManager(this);
        this.passwordManager = new PasswordManager(this);
        this.authManager = new AuthManager(this);

        new CommandManager(this);
        getServer().getPluginManager().registerEvents(authListener, this);

        // Запланированная проверка для показа сообщений
        new BukkitRunnable() {
            @Override
            public void run() {
                for (Player player : getServer().getOnlinePlayers()) {
                    if (!authManager.isLoggedIn(player.getUniqueId())) {
                        authListener.showAuthMessage(player);
                    }
                }
            }
        }.runTaskTimer(this, 100L, 100L); // Проверка каждые 5 секунд (100 тиков)
    }

    public static Main getInstance() { return instance; }
    public AuthListener getAuthListener() { return authListener; }
    public LocaleManager getLocaleManager() { return localeManager; }
    public PasswordManager getPasswordManager() { return passwordManager; }
    public AuthManager getAuthManager() { return authManager; }
}