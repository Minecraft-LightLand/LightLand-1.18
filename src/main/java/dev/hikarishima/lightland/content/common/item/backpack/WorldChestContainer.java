package dev.hikarishima.lightland.content.common.item.backpack;

import dev.hikarishima.lightland.content.common.capability.worldstorage.StorageContainer;
import dev.hikarishima.lightland.init.registrate.MenuRegistrate;
import dev.hikarishima.lightland.util.annotation.ServerOnly;
import dev.lcy0x1.menu.BaseContainerMenu;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MenuType;

import javax.annotation.Nullable;
import java.util.UUID;

public class WorldChestContainer extends BaseContainerMenu<WorldChestContainer> {

	public static WorldChestContainer fromNetwork(MenuType<WorldChestContainer> type, int windowId, Inventory inv) {
		return new WorldChestContainer(windowId, inv, new SimpleContainer(27), null);
	}

	protected final Player player;
	protected final StorageContainer storage;

	public WorldChestContainer(int windowId, Inventory inventory, SimpleContainer cont, @Nullable StorageContainer storage) {
		super(MenuRegistrate.MT_WORLD_CHEST.get(), windowId, inventory, BackpackContainer.MANAGERS[2], menu -> cont, false);
		this.player = inventory.player;
		this.addSlot("grid", stack -> true);
		this.storage = storage;
	}

	@ServerOnly
	@Override
	public boolean stillValid(Player player) {
		return storage == null || storage.isValid();
	}

}
