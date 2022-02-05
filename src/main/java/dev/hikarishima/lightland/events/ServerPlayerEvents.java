package dev.hikarishima.lightland.events;

import dev.hikarishima.lightland.content.archery.feature.bow.EnderShootFeature;
import net.minecraftforge.event.TickEvent;

public class ServerPlayerEvents {

    public void serverPlayerTick(TickEvent.ServerTickEvent event) {
        EnderShootFeature.serverTick();
    }

}
