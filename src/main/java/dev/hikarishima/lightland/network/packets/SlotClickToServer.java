package dev.hikarishima.lightland.network.packets;

import dev.hikarishima.lightland.content.common.item.backpack.BackpackItem;
import dev.hikarishima.lightland.content.common.item.backpack.EnderBackpackItem;
import dev.hikarishima.lightland.network.SerialPacketBase;
import dev.lcy0x1.util.SerialClass;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkHooks;

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
        ServerPlayer player = ctx.getSender();
        if (player == null) return;
        ItemStack stack = ctx.getSender().getInventory().getItem(slot);
        if (stack.getItem() instanceof BackpackItem)
            new BackpackItem.MenuPvd(player, slot, stack).open();
        if (stack.getItem() instanceof EnderBackpackItem)
            NetworkHooks.openGui(player, new SimpleMenuProvider((id, inv, pl) ->
                    ChestMenu.threeRows(id, inv, pl.getEnderChestInventory()), stack.getDisplayName()));
    }
}
