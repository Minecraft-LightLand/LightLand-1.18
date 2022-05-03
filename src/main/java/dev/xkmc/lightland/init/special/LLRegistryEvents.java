package dev.xkmc.lightland.init.special;

import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.NewRegistryEvent;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
@SuppressWarnings("unused")
public class LLRegistryEvents {

	@SubscribeEvent
	public static void onNewRegistry(NewRegistryEvent event) {
		LightLandRegistry.createRegistries(event);
	}

}
