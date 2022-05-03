package dev.xkmc.cuisine.content.tools.base;

import dev.xkmc.l2library.recipe.BaseRecipe;
import dev.xkmc.cuisine.content.tools.base.tile.CuisineTile;
import net.minecraft.resources.ResourceLocation;

public abstract class CuisineRecipe<R extends CuisineRecipe<R, T>, T extends CuisineTile<T>> extends BaseRecipe<R, R, RecipeContainer<T>> {

	public CuisineRecipe(ResourceLocation id, RecType<R, R, RecipeContainer<T>> fac) {
		super(id, fac);
	}

	@Override
	public boolean canCraftInDimensions(int r, int c) {
		return false;
	}


}
