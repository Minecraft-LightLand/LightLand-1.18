package dev.xkmc.cuisine.content.tools.base.handlers;

import dev.xkmc.l2library.serial.SerialClass;
import dev.xkmc.cuisine.content.tools.base.CuisineRecipe;
import dev.xkmc.cuisine.content.tools.base.tile.CookTile;
import dev.xkmc.cuisine.content.tools.base.tile.CuisineTankTile;
import dev.xkmc.cuisine.content.tools.base.tile.CuisineTile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;

import java.util.Optional;

@SerialClass
public class CookHandler<T extends CuisineTile<T> & CookTile, R extends CuisineRecipe<R, T> & TimeHandler.TimeRecipe> {

	private final T tile;
	private final RecipeType<R> type;
	private final ResultHandler<T> handler;
	private final boolean canFail;

	@SerialClass.SerialField
	private ItemStack result = ItemStack.EMPTY;

	@SerialClass.SerialField(toClient = true)
	public int cooking_max = 0, cooking = 0;

	public CookHandler(T tile, RecipeType<R> type, ResultHandler<T> handler, boolean canFail) {
		this.tile = tile;
		this.type = type;
		this.handler = handler;
		this.canFail = canFail;
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
			tile.getMainInventory().clear();
			if (tile instanceof CuisineTankTile<?> tank) tank.fluids.clear();
			if (cooking == 0) {
				handler.addResult(result);
			}
			result = ItemStack.EMPTY;
			cooking = 0;
			cooking_max = 0;
		}
		tile.notifyTile();
	}

	public boolean startCooking() {
		if (tile.getLevel() == null) return false;
		boolean canCook = result.isEmpty() && handler.isEmpty() && !tile.getMainInventory().isEmpty();
		if (!canCook) return false;
		Optional<R> r = tile.getLevel().getRecipeManager().getRecipeFor(type, tile.getMainInventory(), tile.getLevel());
		if (r.isEmpty()) {
			cooking_max = canFail ? 100 : 0;
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
