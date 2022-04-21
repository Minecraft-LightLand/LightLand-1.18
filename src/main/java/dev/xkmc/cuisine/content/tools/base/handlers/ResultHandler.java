package dev.xkmc.cuisine.content.tools.base.handlers;

import dev.xkmc.cuisine.content.tools.base.tile.CuisineTile;
import net.minecraft.world.item.ItemStack;

public abstract class ResultHandler<T extends CuisineTile<T>> {

	protected final T tile;

	public ResultHandler(T tile) {
		this.tile = tile;
	}

	public abstract void addResult(ItemStack stack);

	public abstract boolean isEmpty();
}
