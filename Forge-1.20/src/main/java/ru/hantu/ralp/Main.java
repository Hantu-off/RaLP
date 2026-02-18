package ru.hantu.ralp;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod("ralp")
public class Main {
    public Main() {
        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(new EventHandler());
        PlayerDataStorage.init();
        ConfigManager.init();
    }

    @SubscribeEvent
    public void onRegisterCommands(RegisterCommandsEvent event) {
        CommandRegister.register(event.getDispatcher());
        CommandLogin.register(event.getDispatcher());
        CommandChangePassword.register(event.getDispatcher());
        CommandUnregister.register(event.getDispatcher());
        CommandRalp.register(event.getDispatcher());
    }
}
