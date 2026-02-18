package net.hantu.ralp;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.metadata.FixedMetadataValue;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AuthListener implements Listener {
    private final Main plugin;
    private final Map<UUID, Long> unauthenticatedPlayers = new HashMap<>();
    private final Map<UUID, Long> messageCooldowns = new HashMap<>();
    private static final long MESSAGE_COOLDOWN = 5000;

    public AuthListener(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();

        if (plugin.getAuthManager().hasActiveSession(uuid)) {
            plugin.getAuthListener().restorePlayerState(player);
            unauthenticatedPlayers.remove(uuid);
            return;
        }

        GameMode actualGamemode = player.getGameMode();
        player.setMetadata("previousGamemode", new FixedMetadataValue(plugin, actualGamemode));

        player.setGameMode(GameMode.SPECTATOR);
        player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, Integer.MAX_VALUE, 255, false, false));

        if (plugin.getAuthManager().tryAutoLogin(player)) {
            unauthenticatedPlayers.remove(uuid);
        } else {
            unauthenticatedPlayers.put(uuid, System.currentTimeMillis());
            showAuthMessage(player);
        }
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();

        if (unauthenticatedPlayers.containsKey(uuid)) {
            Location from = event.getFrom();
            Location to = event.getTo();

            if (to != null && !from.toVector().equals(to.toVector())) {
                event.setTo(from);

                long currentTime = System.currentTimeMillis();
                Long lastMessageTime = messageCooldowns.get(uuid);

                if (lastMessageTime == null || currentTime - lastMessageTime >= MESSAGE_COOLDOWN) {
                    showAuthMessage(player);
                    messageCooldowns.put(uuid, currentTime);
                }
            }
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (unauthenticatedPlayers.containsKey(event.getPlayer().getUniqueId())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        if (unauthenticatedPlayers.containsKey(event.getPlayer().getUniqueId())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();

        if (unauthenticatedPlayers.containsKey(uuid)) {
            String msg = event.getMessage().toLowerCase();
            if (!(msg.startsWith("/login") || msg.startsWith("/register") || msg.startsWith("/reg ") || msg.startsWith("/l "))) {
                event.setCancelled(true);
                plugin.adventure().player(player).sendMessage(
                        plugin.getLocaleManager().getMessageComponent("errors.not-logged-in")
                );
            }
        }
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player player) {
            if (unauthenticatedPlayers.containsKey(player.getUniqueId())) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onFly(PlayerToggleFlightEvent event) {
        Player player = event.getPlayer();
        if (unauthenticatedPlayers.containsKey(player.getUniqueId())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
        if (unauthenticatedPlayers.containsKey(event.getPlayer().getUniqueId())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onEntityTarget(EntityTargetEvent event) {
        if (event.getTarget() instanceof Player player) {
            if (unauthenticatedPlayers.containsKey(player.getUniqueId())) {
                event.setCancelled(true);
            }
        }
    }

    public void showAuthMessage(Player player) {
        if (!player.isOnline()) return;

        String messageKey = plugin.getPasswordManager().isPlayerRegistered(player)
                ? "login.usage"
                : "register.usage";

        Component message = plugin.getLocaleManager().getMessageComponent(messageKey);
        plugin.adventure().player(player).sendMessage(message);

        Title title = Title.title(
                Component.empty(),
                message,
                Title.Times.times(Duration.ofMillis(500), Duration.ofMillis(3500), Duration.ofMillis(1000))
        );
        plugin.adventure().player(player).showTitle(title);

        if (plugin.getConfig().getBoolean("settings.sound.enabled", true)) {
            String soundName = plugin.getConfig().getString("settings.sound.name", "entity.ghast.ambient");
            float volume = (float) plugin.getConfig().getDouble("settings.sound.volume", 1.0);
            float pitch = (float) plugin.getConfig().getDouble("settings.sound.pitch", 1.0);

            try {
                player.playSound(player.getLocation(), soundName, volume, pitch);
            } catch (Exception e) {
                plugin.getLogger().warning("Failed to play sound '" + soundName + "': " + e.getMessage());
            }
        }
    }

    public void restorePlayerState(Player player) {
        player.removePotionEffect(PotionEffectType.BLINDNESS);
        player.setGameMode(GameMode.SURVIVAL);
    }

    public void markAsAuthenticated(Player player) {
        UUID uuid = player.getUniqueId();
        unauthenticatedPlayers.remove(uuid);
        messageCooldowns.remove(uuid);
        restorePlayerState(player);
    }
    public boolean isAuthenticated(Player player) {
        return !unauthenticatedPlayers.containsKey(player.getUniqueId());
    }
}