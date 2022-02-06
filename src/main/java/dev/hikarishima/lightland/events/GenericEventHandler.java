package dev.hikarishima.lightland.events;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import dev.hikarishima.lightland.content.common.command.BaseCommand;
import dev.hikarishima.lightland.util.RayTraceUtil;
import dev.lcy0x1.util.SpriteManager;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.function.Consumer;

@SuppressWarnings("unused")
public class GenericEventHandler {

    @SubscribeEvent
    public static void onCommandRegister(RegisterCommandsEvent event) {
        LiteralArgumentBuilder<CommandSourceStack> lightland = Commands.literal("lightland");
        for (Consumer<LiteralArgumentBuilder<CommandSourceStack>> command : BaseCommand.LIST) {
            command.accept(lightland);
        }
        event.getDispatcher().register(lightland);
    }

    @SubscribeEvent
    public static void onAddReloadListenerEvent(AddReloadListenerEvent event) {
        event.addListener(new BaseJsonReloadListener(map -> {
            SpriteManager.CACHE.clear();
            SpriteManager.CACHE.putAll(map);
        }));
    }

    @SubscribeEvent
    public void serverTick(TickEvent.ServerTickEvent event) {
        RayTraceUtil.serverTick();
    }

}
