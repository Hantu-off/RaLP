package ru.hantu.ralp;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

public class CommandUnregister {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("unreg")
                .requires(src -> src.hasPermission(4))
                .then(Commands.argument("player", EntityArgument.player())
                        .executes(CommandUnregister::execute)));
    }

    private static int execute(CommandContext<CommandSourceStack> ctx) {
        ServerPlayer target;
        try {
            target = EntityArgument.getPlayer(ctx, "player");
        } catch (CommandSyntaxException e) {
            ctx.getSource().sendFailure(Component.literal("§cPlayer not found!"));
            return 0;
        }

        var uuid = target.getUUID();

        if (!PlayerDataStorage.isRegistered(uuid)) {
            ctx.getSource().sendFailure(Messages.get("unreg.not-registered"));
            return 0;
        }

        PlayerDataStorage.unregister(uuid);
        AuthManager.setAuthenticated(target, false);

        if (target.isAlive()) {
            target.connection.disconnect(Component.literal("§cYour account has been unregistered by admin."));
        }

        String msg = Messages.get("unreg.success").getString().replace("{player}", target.getName().getString());
        ctx.getSource().sendSuccess(() -> Component.literal(msg), true);
        return 1;
    }
}