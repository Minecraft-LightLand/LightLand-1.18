package dev.xkmc.cuisine.content.tools.base.handlers;

import dev.xkmc.l2library.serial.SerialClass;
import dev.xkmc.cuisine.content.tools.base.CuisineRecipe;
import dev.xkmc.cuisine.content.tools.base.tile.CuisineTile;
import dev.xkmc.cuisine.content.tools.base.tile.StepTile;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeType;

import java.util.Optional;

@SerialClass
public class StepHandler<T extends CuisineTile<T> & StepTile, R extends CuisineRecipe<R, T> & StepHandler.StepRecipe> {


	public interface StepRecipe {

		int getStep();

	}

	private final T tile;
	private final RecipeType<R> type;

	@SerialClass.SerialField(toClient = true)
	public int max_step, step;

	protected ResourceLocation recipe;

	public StepHandler(T tile, RecipeType<R> type) {
		this.tile = tile;
		this.type = type;
	}

	public boolean step() {
		if (step > 0 && tile.getLevel() != null) {
			step--;
			if (!tile.getLevel().isClientSide && step == 0) {
				completeRecipe();
			}
			return true;
		}
		return false;
	}

	public void completeRecipe() {
		if (tile.getLevel() == null || tile.getLevel().isClientSide) return;
		Optional<R> optional = tile.getLevel().getRecipeManager().getRecipeFor(type, tile.getMainInventory(), tile.getLevel());
		max_step = 0;
		step = 0;
		if (optional.isPresent()) {
			R r = optional.get();
			r.assemble(tile.getMainInventory());
		}
		tile.notifyTile();
	}

	public void resetProgress() {
		max_step = 0;
		step = 0;
		recipe = null;
		if (tile.getLevel() != null) {
			Optional<R> optional = tile.getLevel().getRecipeManager().getRecipeFor(type, tile.getMainInventory(), tile.getLevel());
			if (optional.isPresent()) {
				R r = optional.get();
				max_step = r.getStep();
				step = max_step;
				recipe = r.id;
			}
		}
	}


}
