package ru.hantu.ralp;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.level.ServerPlayer;

public class CommandChangePassword {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("changepassword")
                .then(Commands.argument("old", StringArgumentType.word())
                        .then(Commands.argument("new", StringArgumentType.word())
                                .then(Commands.argument("confirm", StringArgumentType.word())
                                        .executes(CommandChangePassword::execute)))));
    }

    private static int execute(CommandContext<CommandSourceStack> ctx) {
        var source = ctx.getSource();
        if (!source.isPlayer()) {
            source.sendFailure(Messages.get("errors.player-only"));
            return 0;
        }

        ServerPlayer player = (ServerPlayer) source.getEntity();
        String oldPass = StringArgumentType.getString(ctx, "old");
        String newPass = StringArgumentType.getString(ctx, "new");
        String confirm = StringArgumentType.getString(ctx, "confirm");

        if (!newPass.equals(confirm)) {
            player.sendSystemMessage(Messages.get("register.passwords-not-match"));
            return 0;
        }

        if (newPass.length() < ConfigManager.minPasswordLength) {
            player.sendSystemMessage(Messages.get("register.too-short"));
            return 0;
        }

        String hash = PlayerDataStorage.getPasswordHash(player.getUUID());
        if (hash == null || !PasswordManager.verify(oldPass, hash)) {
            player.sendSystemMessage(Messages.get("login.wrong"));
            return 0;
        }

        PlayerDataStorage.updatePassword(player, PasswordManager.hash(newPass));
        AuthManager.setAuthenticated(player, false);
        player.sendSystemMessage(Messages.get("changepassword.success"));
        return 1;
    }
}