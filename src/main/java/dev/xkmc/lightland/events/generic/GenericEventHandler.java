package dev.xkmc.lightland.events.generic;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import dev.xkmc.lightland.content.common.command.BaseCommand;
import dev.xkmc.lightland.util.RayTraceUtil;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
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

}
