package dev.hikarishima.lightland.events;

import dev.hikarishima.lightland.init.registrate.VanillaMagicRegistrate;
import net.minecraftforge.event.entity.living.LivingKnockBackEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class ProfessionEventHandler {

    @SubscribeEvent
    public static void onEntityKnockBack(LivingKnockBackEvent event) {
        if (event.getEntityLiving().hasEffect(VanillaMagicRegistrate.NO_KB.get()))
            event.setCanceled(true);
    }

}
