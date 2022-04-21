package dev.xkmc.cuisine.compat.jei.recipes;

import dev.xkmc.cuisine.content.tools.mortar.MortarRecipe;
import dev.xkmc.cuisine.init.registrate.CuisineBlocks;
import dev.xkmc.cuisine.init.registrate.CuisineRecipes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.recipe.IFocusGroup;
import net.minecraft.MethodsReturnNonnullByDefault;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class MortarRecipeCategory extends BaseCuisineRecipeCategory<MortarRecipe, MortarRecipeCategory> {

	public MortarRecipeCategory() {
		super("mortar", CuisineBlocks.MORTAR, MortarRecipe.class, 0, CuisineRecipes.RT_MORTAR.get());
	}

	@Override
	public void setRecipe(IRecipeLayoutBuilder builder, MortarRecipe recipe, IFocusGroup focuses) {
		addInputItem(builder, 0, recipe.ingredient);
		addOutputItem(builder, 0, recipe.result);
	}

}
