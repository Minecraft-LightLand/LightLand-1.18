package dev.hikarishima.lightland.events.generic;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import dev.hikarishima.lightland.content.common.command.BaseCommand;
import dev.hikarishima.lightland.network.config.ConfigSyncManager;
import dev.hikarishima.lightland.util.math.RayTraceUtil;
import dev.lcy0x1.menu.OverlayManager;
import dev.lcy0x1.menu.SpriteManager;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RegisterClientReloadListenersEvent;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.event.OnDatapackSyncEvent;
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
	public static void serverTick(TickEvent.ServerTickEvent event) {
		RayTraceUtil.serverTick();
	}


	@SubscribeEvent
	public static void addReloadListeners(AddReloadListenerEvent event) {
		event.addListener(new BaseJsonReloadListener("gui/coords", map -> {
			SpriteManager.CACHE.clear();
			SpriteManager.CACHE.putAll(map);
		}));
		event.addListener(ConfigSyncManager.CONFIG);
	}

	@OnlyIn(Dist.CLIENT)
	public static void clientReloadListeners(RegisterClientReloadListenersEvent event) {
		event.registerReloadListener(new BaseJsonReloadListener("textures/gui/overlays", map -> {
			map.forEach((k, v) -> {
				String modid = k.getNamespace();
				String[] paths = k.getPath().split("/");
				String path = paths[paths.length - 1];
				OverlayManager.get(modid, path).reset(v);
			});
		}));
	}

	@SubscribeEvent
	public static void onDatapackSync(OnDatapackSyncEvent event) {
		ConfigSyncManager.onDatapackSync(event);
	}

}
