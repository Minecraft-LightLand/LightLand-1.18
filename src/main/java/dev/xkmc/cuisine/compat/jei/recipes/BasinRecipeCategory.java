package dev.xkmc.cuisine.compat.jei.recipes;

import dev.xkmc.cuisine.content.tools.basin.BasinBlockEntity;
import dev.xkmc.cuisine.content.tools.basin.BasinRecipe;
import dev.xkmc.cuisine.init.registrate.CuisineBlocks;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.recipe.IFocusGroup;
import net.minecraft.MethodsReturnNonnullByDefault;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class BasinRecipeCategory extends BaseCuisineRecipeCategory<BasinRecipe, BasinRecipeCategory> {

	public BasinRecipeCategory() {
		super("basin", CuisineBlocks.BASIN, BasinRecipe.class, BasinBlockEntity.MAX_FLUID);
	}

	@Override
	public void setRecipe(IRecipeLayoutBuilder builder, BasinRecipe recipe, IFocusGroup focuses) {
		int index = 0;
		if (!recipe.item_ingredients.isEmpty())
			addInputItem(builder, index++, recipe.item_ingredients);
		if (!recipe.fluid_ingredient.isEmpty())
			addInputFluid(builder, index, recipe.fluid_ingredient);
		index = 0;
		if (!recipe.result.isEmpty())
			addOutputItem(builder, index++, recipe.result);
		if (!recipe.remain.isEmpty())
			addOutputFluid(builder, index, recipe.remain);
	}

}
