package dev.xkmc.cuisine.compat.jei.recipes;

import dev.xkmc.cuisine.content.tools.basin.BasinBlockEntity;
import dev.xkmc.cuisine.content.tools.basin.BasinDryRecipe;
import dev.xkmc.cuisine.init.registrate.CuisineBlocks;
import dev.xkmc.cuisine.init.registrate.CuisineRecipes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.recipe.IFocusGroup;
import net.minecraft.MethodsReturnNonnullByDefault;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class BasinDryRecipeCategory extends BaseCuisineRecipeCategory<BasinDryRecipe, BasinDryRecipeCategory> {

	public BasinDryRecipeCategory() {
		super("basin_dry", CuisineBlocks.BASIN, BasinDryRecipe.class, BasinBlockEntity.MAX_FLUID, CuisineRecipes.RT_BASIN_DRY.get());
	}

	@Override
	public void setRecipe(IRecipeLayoutBuilder builder, BasinDryRecipe recipe, IFocusGroup focuses) {
		if (!recipe.fluid_ingredient.isEmpty())
			addInputFluid(builder, 0, recipe.fluid_ingredient);
		if (!recipe.result.isEmpty())
			addOutputItem(builder, 0, recipe.result);
	}

}
