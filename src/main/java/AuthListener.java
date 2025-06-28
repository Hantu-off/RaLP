package net.hantu.ralp;

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

        // Сохраняем предыдущий режим игры
        player.setMetadata("previousGamemode", new FixedMetadataValue(plugin, player.getGameMode()));

        // Устанавливаем режим наблюдателя и слепоту
        player.setGameMode(GameMode.SPECTATOR);
        player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, Integer.MAX_VALUE, 255, false, false));

        // Пробуем авто-вход
        if (plugin.getAuthManager().tryAutoLogin(player)) {
            // Если авто-вход успешен, сразу восстанавливаем состояние
            restorePlayerState(player);
            unauthenticatedPlayers.remove(uuid);
        } else {
            // Если авто-вход не сработал, добавляем в список неавторизованных и показываем сообщение
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

            if (to != null && (from.getX() != to.getX() || from.getY() != to.getY() || from.getZ() != to.getZ())) {
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
            if (!(msg.startsWith("/login") || msg.startsWith("/register") || msg.startsWith("/r ") || msg.startsWith("/l "))) {
                event.setCancelled(true);
                player.sendMessage(plugin.getLocaleManager().getMessage("errors.not-logged-in"));
            }
        }
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
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
        if (event.getTarget() instanceof Player) {
            Player player = (Player) event.getTarget();
            if (unauthenticatedPlayers.containsKey(player.getUniqueId())) {
                event.setCancelled(true);
            }
        }
    }

    public void showAuthMessage(Player player) {
        if (!player.isOnline()) return;

        String message = plugin.getPasswordManager().isPlayerRegistered(player)
                ? plugin.getLocaleManager().getMessage("login.usage")
                : plugin.getLocaleManager().getMessage("register.usage");

        player.sendMessage(message);
        player.sendTitle("", message, 10, 70, 20);
    }

    public void restorePlayerState(Player player) {
        player.removePotionEffect(PotionEffectType.BLINDNESS);

        if (player.hasMetadata("previousGamemode")) {
            GameMode previousMode = (GameMode) player.getMetadata("previousGamemode").get(0).value();
            player.setGameMode(previousMode);
            player.removeMetadata("previousGamemode", plugin);
        } else {
            player.setGameMode(GameMode.SURVIVAL);
        }
    }

    public void markAsAuthenticated(Player player) {
        UUID uuid = player.getUniqueId();
        unauthenticatedPlayers.remove(uuid);
        messageCooldowns.remove(uuid);
        restorePlayerState(player);
    }
}