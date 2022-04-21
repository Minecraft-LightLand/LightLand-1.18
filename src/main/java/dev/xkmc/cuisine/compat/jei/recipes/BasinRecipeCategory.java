package dev.xkmc.cuisine.compat.jei.recipes;

import dev.xkmc.cuisine.content.tools.basin.BasinBlockEntity;
import dev.xkmc.cuisine.content.tools.basin.BasinRecipe;
import dev.xkmc.cuisine.init.registrate.CuisineBlocks;
import dev.xkmc.cuisine.init.registrate.CuisineRecipes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.recipe.IFocusGroup;
import net.minecraft.MethodsReturnNonnullByDefault;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class BasinRecipeCategory extends BaseCuisineRecipeCategory<BasinRecipe, BasinRecipeCategory> {

	public BasinRecipeCategory() {
		super("basin", CuisineBlocks.BASIN, BasinRecipe.class, BasinBlockEntity.MAX_FLUID, CuisineRecipes.RT_BASIN.get());
	}

	@Override
	public void setRecipe(IRecipeLayoutBuilder builder, BasinRecipe recipe, IFocusGroup focuses) {
		if (!recipe.item_ingredients.isEmpty())
			addInputItem(builder, 0, recipe.item_ingredients);
		if (!recipe.remain.isEmpty())
			addOutputFluid(builder, 0, recipe.remain);
	}

}
