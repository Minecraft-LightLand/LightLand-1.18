package dev.xkmc.cuisine.content.tools.base.handlers;

import dev.xkmc.l2library.serial.SerialClass;
import dev.xkmc.cuisine.content.tools.base.CuisineRecipe;
import dev.xkmc.cuisine.content.tools.base.tile.CuisineTile;
import dev.xkmc.cuisine.content.tools.base.tile.TimeTile;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeType;

import java.util.Optional;

@SerialClass
public class TimeHandler<T extends CuisineTile<T> & TimeTile, R extends CuisineRecipe<R, T> & TimeHandler.TimeRecipe> {

	public interface TimeRecipe {

		int getTime();

	}

	private final T tile;
	private final RecipeType<R> type;

	@SerialClass.SerialField(toClient = true)
	public int max_time, time;

	protected ResourceLocation recipe;

	public TimeHandler(T tile, RecipeType<R> type) {
		this.tile = tile;
		this.type = type;
	}

	public boolean processing() {
		return time > 0;
	}

	public boolean tick() {
		if (time > 0 && tile.getLevel() != null) {
			time--;
			if (!tile.getLevel().isClientSide && time == 0) {
				completeRecipe();
			}
			return true;
		}
		return false;
	}

	public void completeRecipe() {
		if (tile.getLevel() == null || tile.getLevel().isClientSide) return;
		Optional<R> optional = tile.getLevel().getRecipeManager().getRecipeFor(type, tile.getMainInventory(), tile.getLevel());
		max_time = 0;
		time = 0;
		if (optional.isPresent()) {
			R r = optional.get();
			r.assemble(tile.getMainInventory());
		}
		tile.notifyTile();
	}

	public void resetProgress() {
		max_time = 0;
		time = 0;
		recipe = null;
		if (tile.getLevel() != null) {
			Optional<R> optional = tile.getLevel().getRecipeManager().getRecipeFor(type, tile.getMainInventory(), tile.getLevel());
			if (optional.isPresent()) {
				R r = optional.get();
				max_time = r.getTime();
				time = max_time;
				recipe = r.id;
			}
		}
	}

}
