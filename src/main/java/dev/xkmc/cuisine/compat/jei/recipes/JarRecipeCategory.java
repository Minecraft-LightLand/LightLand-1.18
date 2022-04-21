package dev.xkmc.cuisine.compat.jei.recipes;

import dev.xkmc.cuisine.content.tools.jar.JarBlockEntity;
import dev.xkmc.cuisine.content.tools.jar.JarRecipe;
import dev.xkmc.cuisine.init.registrate.CuisineBlocks;
import dev.xkmc.cuisine.init.registrate.CuisineRecipes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.recipe.IFocusGroup;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.world.item.crafting.Ingredient;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class JarRecipeCategory extends BaseCuisineRecipeCategory<JarRecipe, JarRecipeCategory> {

	public JarRecipeCategory() {
		super("jar", CuisineBlocks.JAR, JarRecipe.class, JarBlockEntity.MAX_FLUID, CuisineRecipes.RT_JAR.get());
	}

	@Override
	public void setRecipe(IRecipeLayoutBuilder builder, JarRecipe recipe, IFocusGroup focuses) {
		int index = 0;
		for (Ingredient ingredient : recipe.item_ingredients) {
			addInputItem(builder, index++, ingredient);
		}
		if (!recipe.fluid_ingredient.isEmpty())
			addInputFluid(builder, index, recipe.fluid_ingredient);
		index = 0;
		if (!recipe.result.isEmpty())
			addOutputItem(builder, index++, recipe.result);
		if (!recipe.remain.isEmpty())
			addOutputFluid(builder, index, recipe.remain);
	}

}
