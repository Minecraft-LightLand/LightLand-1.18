package dev.hikarishima.lightland.events;

import dev.hikarishima.lightland.content.common.item.backpack.BackpackItem;
import dev.hikarishima.lightland.network.packets.SlotClickToServer;
import dev.lcy0x1.base.Proxy;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.world.inventory.Slot;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class MiscEventHadler {

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void onScreenClick(ScreenEvent.MouseClickedEvent event) {
        Screen screen = event.getScreen();
        if (event.getButton() == 1 &&
                screen instanceof AbstractContainerScreen cont) {
            Slot slot = cont.findSlot(event.getMouseX(), event.getMouseY());
            if (slot != null &&
                    slot.container == Proxy.getClientPlayer().getInventory() &&
                    slot.getItem().getItem() instanceof BackpackItem) {
                int ind = slot.getSlotIndex();
                new SlotClickToServer(ind).toServer();
                event.setCanceled(true);
            }
        }
    }

}
