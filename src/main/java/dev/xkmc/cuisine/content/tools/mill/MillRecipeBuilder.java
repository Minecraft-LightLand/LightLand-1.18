package dev.xkmc.cuisine.content.tools.mill;

import dev.xkmc.l2library.recipe.BaseRecipeBuilder;
import dev.xkmc.cuisine.content.tools.base.RecipeContainer;
import dev.xkmc.cuisine.init.registrate.CuisineRecipes;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.fluids.FluidStack;

public class MillRecipeBuilder extends BaseRecipeBuilder<MillRecipeBuilder, MillRecipe, MillRecipe, RecipeContainer<MillBlockEntity>> {

	public MillRecipeBuilder(Ingredient in, FluidStack out, int water, int step) {
		super(CuisineRecipes.RS_MILL.get());
		recipe.item_ingredients = in;
		recipe.fluid_ingredient = water == 0 ? FluidStack.EMPTY : new FluidStack(Fluids.WATER, water);
		recipe.remain = out;
		recipe.step = step;
	}

}
