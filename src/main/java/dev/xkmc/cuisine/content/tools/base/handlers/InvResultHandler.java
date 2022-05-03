package dev.xkmc.cuisine.content.tools.base.handlers;

import dev.xkmc.cuisine.content.tools.base.tile.CuisineTile;
import net.minecraft.world.item.ItemStack;

public class InvResultHandler<T extends CuisineTile<T>> extends ResultHandler<T> {

	public InvResultHandler(T tile) {
		super(tile);
	}

	@Override
	public void addResult(ItemStack stack) {
		tile.inventory.addItem(stack);
	}

	@Override
	public boolean isEmpty() {
		return tile.inventory.countSpace() > 0;
	}

}
