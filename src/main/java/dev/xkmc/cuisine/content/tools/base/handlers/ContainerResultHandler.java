package dev.xkmc.cuisine.content.tools.base.handlers;

import dev.xkmc.l2library.serial.SerialClass;
import dev.xkmc.cuisine.content.tools.base.tile.BottleResultTile;
import dev.xkmc.cuisine.content.tools.base.tile.CuisineTile;
import net.minecraft.world.item.ItemStack;

@SerialClass
public class ContainerResultHandler<T extends CuisineTile<T> & BottleResultTile> extends ResultHandler<T> {

	@SerialClass.SerialField(toClient = true)
	public ItemStack result = ItemStack.EMPTY;

	public ContainerResultHandler(T tile) {
		super(tile);
	}

	@Override
	public void addResult(ItemStack stack) {
		result = stack;
	}

	@Override
	public boolean isEmpty() {
		return result.isEmpty();
	}

}
