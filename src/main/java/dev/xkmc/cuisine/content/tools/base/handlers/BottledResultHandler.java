package dev.xkmc.cuisine.content.tools.base.handlers;

import dev.lcy0x1.serial.SerialClass;
import dev.xkmc.cuisine.content.tools.base.tile.BottleResultTile;
import dev.xkmc.cuisine.content.tools.base.tile.CuisineTile;
import net.minecraft.world.item.ItemStack;

@SerialClass
public class BottledResultHandler<T extends CuisineTile<T> & BottleResultTile> extends ResultHandler<T> {

	@SerialClass.SerialField(toClient = true)
	public ItemStack result = ItemStack.EMPTY;

	public BottledResultHandler(T tile) {
		super(tile);
	}

	@Override
	public void addResult(ItemStack stack) {

	}

	@Override
	public boolean isEmpty() {
		return result.isEmpty();
	}

}
