package dev.hikarishima.lightland.network.packets;

import dev.hikarishima.lightland.content.common.item.backpack.BackpackItem;
import dev.hikarishima.lightland.network.SerialPacketBase;
import dev.lcy0x1.util.SerialClass;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

@SerialClass
public class SlotClickToServer extends SerialPacketBase {

    @SerialClass.SerialField
    public int slot;

    @Deprecated
    public SlotClickToServer() {

    }

    public SlotClickToServer(int slot) {
        this.slot = slot;
    }

    @Override
    public void handle(NetworkEvent.Context ctx) {
        if (ctx.getSender() == null) return;
        ItemStack stack = ctx.getSender().getInventory().getItem(slot);
        if (stack.getItem() instanceof BackpackItem)
            new BackpackItem.MenuPvd(ctx.getSender(), slot, stack).open();
    }
}
