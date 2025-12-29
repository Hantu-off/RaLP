package net.hantu.ralp;

import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

public class Main extends JavaPlugin {
    private static Main instance;
    private AuthListener authListener;
    private LocaleManager localeManager;
    private PasswordManager passwordManager;
    private AuthManager authManager;
    private BukkitAudiences adventure;

    public enum AuthState {
        REGISTERING, LOGGING_IN
    }
    private final Map<UUID, AuthState> authStates = new HashMap<>();

    @Override
    public void onEnable() {
        instance = this;

        boolean isFirstRun = !getDataFolder().exists() || !new java.io.File(getDataFolder(), "config.yml").exists();
        if (isFirstRun) {
            setupDefaultConfigWithAutoLanguage();
        } else {
            saveDefaultConfig();
        }

        saveResource("messages.yml", false);

        this.adventure = BukkitAudiences.create(this);
        this.authListener = new AuthListener(this);
        this.localeManager = new LocaleManager(this);
        this.passwordManager = new PasswordManager(this);
        this.authManager = new AuthManager(this);

        new CommandManager(this);
        getServer().getPluginManager().registerEvents(authListener, this);
    }

    private void setupDefaultConfigWithAutoLanguage() {
        getDataFolder().mkdirs();

        String detectedLang = detectServerLanguage();
        getLogger().info("Detected server language: " + detectedLang);

        saveDefaultConfig();

        getConfig().set("settings.default-language", detectedLang);
        saveConfig();
    }

    private String detectServerLanguage() {
        Locale serverLocale = Locale.getDefault();
        String lang = serverLocale.getLanguage().toLowerCase();
        String country = serverLocale.getCountry().toLowerCase();

        Map<String, String> supported = new HashMap<>();
        supported.put("en", "en_us");
        supported.put("ru", "ru_ru");
        supported.put("de", "de_de");
        supported.put("es", "es_es");
        supported.put("pt", "pt_pt");
        supported.put("fr", "fr_fr");
        supported.put("tr", "tr_tr");
        supported.put("it", "it_it");
        supported.put("ko", "ko_kr");
        supported.put("ja", "ja_jp");
        supported.put("zh", "zh_cn");
        supported.put("pl", "pl_pl");

        String fullTag = lang + "_" + country;
        for (String code : supported.values()) {
            if (code.equalsIgnoreCase(fullTag)) {
                return code;
            }
        }

        return supported.getOrDefault(lang, "en_us");
    }

    @Override
    public void onDisable() {
        if (this.authManager != null) {
            this.authManager.saveAutoLoginData();
        }
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
    public Map<UUID, AuthState> getAuthStates() { return authStates; }
}