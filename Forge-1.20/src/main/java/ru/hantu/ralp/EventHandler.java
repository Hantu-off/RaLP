package ru.hantu.ralp;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.CommandEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class EventHandler {

    private static final java.util.Map<java.util.UUID, Vec3> spawnPositions = new java.util.HashMap<>();

    @SubscribeEvent
    public void onJoin(PlayerEvent.PlayerLoggedInEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) return;

        AuthManager.setAuthenticated(player, false);
        player.setGameMode(net.minecraft.world.level.GameType.SPECTATOR);

        spawnPositions.put(player.getUUID(), player.position());

        if (PlayerDataStorage.isRegistered(player.getUUID())) {
            player.sendSystemMessage(Messages.get("login.usage"));
        } else {
            player.sendSystemMessage(Messages.get("register.usage"));
        }
    }

    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.START) return;
        if (!(event.player instanceof ServerPlayer player)) return;
        if (AuthManager.isAuthenticated(player)) return;

        Vec3 spawn = spawnPositions.get(player.getUUID());
        if (spawn == null) return;

        if (player.getX() != spawn.x || player.getY() != spawn.y || player.getZ() != spawn.z) {
            player.teleportTo(spawn.x, spawn.y, spawn.z);
        }
    }

    @SubscribeEvent
    public void onCommand(CommandEvent event) {
        var source = event.getParseResults().getContext().getSource();
        if (!source.isPlayer()) return;

        ServerPlayer player = (ServerPlayer) source.getEntity();
        if (AuthManager.isAuthenticated(player)) return;

        var nodes = event.getParseResults().getContext().getNodes();
        if (nodes.isEmpty()) {
            event.setCanceled(true);
            player.sendSystemMessage(Messages.get("errors.not-logged-in"));
            return;
        }

        String commandName = nodes.get(0).getNode().getName();
        if ("login".equals(commandName) ||
                "register".equals(commandName) ||
                "changepassword".equals(commandName) ||
                "help".equals(commandName)) {
            return;
        }

        event.setCanceled(true);
        player.sendSystemMessage(Messages.get("errors.not-logged-in"));
    }
}