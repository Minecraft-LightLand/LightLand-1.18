package dev.xkmc.cuisine.content.tools.basin;

import dev.xkmc.l2library.recipe.BaseRecipeBuilder;
import dev.xkmc.cuisine.content.tools.base.RecipeContainer;
import dev.xkmc.cuisine.init.registrate.CuisineRecipes;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.fluids.FluidStack;

public class BasinRecipeBuilder extends BaseRecipeBuilder<BasinRecipeBuilder, BasinRecipe, BasinRecipe, RecipeContainer<BasinBlockEntity>> {

	public BasinRecipeBuilder(Ingredient in, FluidStack out, int step) {
		super(CuisineRecipes.RS_BASIN.get());
		recipe.item_ingredients = in;
		recipe.remain = out;
		recipe.step = step;
	}

}
