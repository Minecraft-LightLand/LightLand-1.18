package dev.xkmc.cuisine.content.tools.base;

import dev.lcy0x1.serial.SerialClass;
import dev.xkmc.cuisine.content.tools.base.tile.BottleResultTile;
import dev.xkmc.cuisine.content.tools.base.tile.CookTile;
import dev.xkmc.cuisine.content.tools.base.tile.CuisineTankTile;
import dev.xkmc.cuisine.content.tools.base.tile.CuisineTile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;

import java.util.Optional;

@SerialClass
public class CookHandler<T extends CuisineTile<T> & BottleResultTile & CookTile, R extends CuisineRecipe<R, T> & TimeHandler.TimeRecipe> {

	private final T tile;
	private final RecipeType<R> type;

	@SerialClass.SerialField(toClient = true)
	public int cooking_max = 0, cooking = 0;
	@SerialClass.SerialField(toClient = true)
	public ItemStack result = ItemStack.EMPTY;

	public CookHandler(T tile, RecipeType<R> type) {
		this.tile = tile;
		this.type = type;
	}

	public boolean tick() {
		if (cooking > 0) {
			cooking--;
			if (tile.getLevel() != null && cooking == 0 && !tile.getLevel().isClientSide()) {
				stopCooking();
				tile.onStopCooking();
			}
			return true;
		}
		return false;
	}

	public void stopCooking() {
		if (cooking_max > 0) {
			if (cooking > 0) {
				result = ItemStack.EMPTY;
			}
			cooking = 0;
			cooking_max = 0;
		}
		tile.notifyTile();
	}

	public boolean startCooking() {
		if (tile.getLevel() == null) return false;
		boolean canCook = result.isEmpty() && !tile.getMainInventory().isEmpty();
		if (!canCook) return false;
		Optional<R> r = tile.getLevel().getRecipeManager().getRecipeFor(type, tile.getMainInventory(), tile.getLevel());
		tile.getMainInventory().clear();
		if (tile instanceof CuisineTankTile<?> tank) tank.fluids.clear();
		if (r.isEmpty()) {
			cooking_max = 100;
			cooking = cooking_max;
			result = ItemStack.EMPTY;
		} else {
			R recipe = r.get();
			cooking_max = recipe.getTime();
			cooking = cooking_max;
			result = recipe.getResultItem().copy();
		}
		tile.notifyTile();
		return true;
	}

}
