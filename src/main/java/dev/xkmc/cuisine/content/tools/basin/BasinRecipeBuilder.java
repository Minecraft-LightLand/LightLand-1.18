package dev.xkmc.cuisine.content.tools.basin;

import dev.lcy0x1.recipe.BaseRecipeBuilder;
import dev.xkmc.cuisine.content.tools.base.RecipeContainer;
import dev.xkmc.cuisine.init.registrate.CuisineRecipes;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.fluids.FluidStack;

import java.util.function.Consumer;

public class BasinRecipeBuilder extends BaseRecipeBuilder<BasinRecipeBuilder, BasinRecipe, BasinRecipe, RecipeContainer<BasinBlockEntity>> {

	public BasinRecipeBuilder(Ingredient in, FluidStack out, int step) {
		super(CuisineRecipes.RS_BASIN.get());
		recipe.item_ingredients = in;
		recipe.remain = out;
		recipe.step = step;
	}

}
