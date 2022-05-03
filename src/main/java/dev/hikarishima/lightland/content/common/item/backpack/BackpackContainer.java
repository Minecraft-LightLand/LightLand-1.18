package dev.hikarishima.lightland.content.common.item.backpack;

import dev.hikarishima.lightland.init.LightLand;
import dev.hikarishima.lightland.init.registrate.LightlandMenu;
import dev.xkmc.l2library.menu.BaseContainerMenu;
import dev.xkmc.l2library.menu.SpriteManager;
import dev.xkmc.l2library.util.ServerOnly;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;

import java.util.UUID;

public class BackpackContainer extends BaseContainerMenu<BackpackContainer> {

	public static final SpriteManager[] MANAGERS = new SpriteManager[6];

	static {
		for (int i = 0; i < 6; i++) {
			MANAGERS[i] = new SpriteManager(LightLand.MODID, "backpack_" + (i + 1));
		}
	}

	public static BackpackContainer fromNetwork(MenuType<BackpackContainer> type, int windowId, Inventory inv, FriendlyByteBuf buf) {
		int slot = buf.readInt();
		UUID id = buf.readUUID();
		int row = buf.readInt();
		return new BackpackContainer(windowId, inv, slot, id, row);
	}

	protected final Player player;
	protected final int item_slot;
	protected final UUID uuid;

	public BackpackContainer(int windowId, Inventory inventory, int hand, UUID uuid, int row) {
		super(LightlandMenu.MT_BACKPACK.get(), windowId, inventory, MANAGERS[row - 1], menu -> new BaseContainer<>(row * 9, menu), false);
		this.player = inventory.player;
		this.item_slot = hand;
		this.uuid = uuid;
		this.addSlot("grid", stack -> stack.getItem().canFitInsideContainerItems());
		if (!this.player.level.isClientSide()) {
			ItemStack stack = getStack();
			if (!stack.isEmpty()) {
				ListTag tag = BackpackItem.getListTag(stack);
				for (int i = 0; i < tag.size(); i++) {
					this.container.setItem(i, ItemStack.of((CompoundTag) tag.get(i)));
				}
			}
		}
	}

	@ServerOnly
	@Override
	public boolean stillValid(Player player) {
		return !getStack().isEmpty();
	}

	@ServerOnly
	public ItemStack getStack() {
		ItemStack stack = player.getInventory().getItem(item_slot);
		CompoundTag tag = stack.getTag();
		if (tag == null) return ItemStack.EMPTY;
		if (!tag.contains("container_id")) return ItemStack.EMPTY;
		if (!tag.getUUID("container_id").equals(uuid)) return ItemStack.EMPTY;
		return stack;
	}


	@Override
	public void removed(Player player) {
		if (!player.level.isClientSide) {
			ItemStack stack = getStack();
			if (!stack.isEmpty()) {
				ListTag list = new ListTag();
				for (int i = 0; i < this.container.getContainerSize(); i++) {
					list.add(i, this.container.getItem(i).save(new CompoundTag()));
				}
				BackpackItem.setListTag(stack, list);
			}
		}
		super.removed(player);
	}

}
