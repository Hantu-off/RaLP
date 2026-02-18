package ru.hantu.ralp;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

public class CommandRegister {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("register")
                .then(Commands.argument("password", StringArgumentType.word())
                        .then(Commands.argument("confirm", StringArgumentType.word())
                                .executes(CommandRegister::execute))));
    }

    private static int execute(CommandContext<CommandSourceStack> ctx) {
        var source = ctx.getSource();
        if (!source.isPlayer()) {
            source.sendFailure(Messages.get("errors.player-only"));
            return 0;
        }

        ServerPlayer player = (ServerPlayer) source.getEntity();
        player.sendSystemMessage(Component.literal("§b[DEBUG] Register executed"));
        String pass1 = StringArgumentType.getString(ctx, "password");
        String pass2 = StringArgumentType.getString(ctx, "confirm");

        if (!pass1.equals(pass2)) {
            player.sendSystemMessage(Messages.get("register.passwords-not-match"));
            return 0;
        }

        if (pass1.length() < ConfigManager.minPasswordLength) {
            player.sendSystemMessage(Messages.get("register.too-short"));
            return 0;
        }

        if (PlayerDataStorage.isRegistered(player.getUUID())) {
            player.sendSystemMessage(Messages.get("register.already"));
            return 0;
        }

        String hash;
        try {
            hash = PasswordManager.hash(pass1);
        } catch (Exception e) {
            player.sendSystemMessage(Component.literal("§c[ERROR] Password hashing failed!"));
            e.printStackTrace();
            return 0;
        }

        player.sendSystemMessage(Component.literal("§b[DEBUG] Register executed 2"));
        PlayerDataStorage.register(player, hash);
        AuthManager.setAuthenticated(player, true);
        player.setGameMode(net.minecraft.world.level.GameType.SURVIVAL);
        player.sendSystemMessage(Messages.get("register.success"));
        return 1;
    }
}