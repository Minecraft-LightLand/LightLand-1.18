package dev.hikarishima.lightland.events;

import dev.hikarishima.lightland.content.archery.feature.bow.EnderShootFeature;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class ServerPlayerEvents {

    @SubscribeEvent
    public void serverPlayerTick(TickEvent.ServerTickEvent event) {
        EnderShootFeature.serverTick();
    }

}
