package dev.xkmc.cuisine.content.tools.base.tile;

import net.minecraft.world.item.ItemStack;

public interface BottleResultTile {

	ItemStack getResult();

	void clearResult();

	boolean canTake();

}
