package dev.hikarishima.lightland.network.packets;

import dev.hikarishima.lightland.content.common.item.backpack.BackpackItem;
import dev.hikarishima.lightland.content.common.item.backpack.EnderBackpackItem;
import dev.hikarishima.lightland.content.common.item.backpack.WorldChestItem;
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

	/**
	 * slot click for backpack
	 */
	@SerialClass.SerialField
	private int index, slot;

	@Deprecated
	public SlotClickToServer() {

	}

	public SlotClickToServer(int index, int slot) {
		this.index = index;
		this.slot = slot;
	}

	@Override
	public void handle(NetworkEvent.Context ctx) {
		ServerPlayer player = ctx.getSender();
		if (player == null) return;
		ItemStack stack;
		if (slot >= 0) stack = ctx.getSender().getInventory().getItem(slot);
		else stack = ctx.getSender().containerMenu.getSlot(index).getItem();
		if (slot >= 0 && stack.getItem() instanceof BackpackItem) {
			new BackpackItem.MenuPvd(player, slot, stack).open();
		} else if (stack.getItem() instanceof EnderBackpackItem) {
			NetworkHooks.openGui(player, new SimpleMenuProvider((id, inv, pl) ->
					ChestMenu.threeRows(id, inv, pl.getEnderChestInventory()), stack.getDisplayName()));
		} else if (stack.getItem() instanceof WorldChestItem chest) {
			new WorldChestItem.MenuPvd(player, stack, chest).open();
		}
	}
}
