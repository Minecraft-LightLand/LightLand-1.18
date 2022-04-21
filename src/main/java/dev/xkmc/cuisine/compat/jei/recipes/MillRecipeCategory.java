package dev.xkmc.cuisine.compat.jei.recipes;

import dev.xkmc.cuisine.content.tools.mill.MillBlockEntity;
import dev.xkmc.cuisine.content.tools.mill.MillRecipe;
import dev.xkmc.cuisine.init.registrate.CuisineBlocks;
import dev.xkmc.cuisine.init.registrate.CuisineRecipes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.recipe.IFocusGroup;
import net.minecraft.MethodsReturnNonnullByDefault;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class MillRecipeCategory extends BaseCuisineRecipeCategory<MillRecipe, MillRecipeCategory> {

	public MillRecipeCategory() {
		super("mill", CuisineBlocks.MILL, MillRecipe.class, MillBlockEntity.MAX_FLUID, CuisineRecipes.RT_MILL.get());
	}

	@Override
	public void setRecipe(IRecipeLayoutBuilder builder, MillRecipe recipe, IFocusGroup focuses) {
		int index = 0;
		if (!recipe.item_ingredients.isEmpty())
			addInputItem(builder, index++, recipe.item_ingredients);
		if (!recipe.fluid_ingredient.isEmpty())
			addInputFluid(builder, index, recipe.fluid_ingredient);
		index = 0;
		if (!recipe.remain.isEmpty())
			addOutputFluid(builder, index, recipe.remain);
	}
}
