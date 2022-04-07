package dev.hikarishima.lightland.init;

import dev.hikarishima.lightland.events.generic.GenericEventHandler;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

public class LightLandClient {

	public static void onCtorClient(IEventBus bus, IEventBus eventBus) {
		bus.addListener(GenericEventHandler::clientReloadListeners);
	}

	@SubscribeEvent
	public static void clientSetup(FMLClientSetupEvent event) {
		ClientRegister.registerItemProperties();
		ClientRegister.registerOverlays();
		ClientRegister.registerKeys();
	}

}
