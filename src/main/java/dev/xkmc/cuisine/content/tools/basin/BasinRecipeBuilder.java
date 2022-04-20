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
		recipe.fluid_ingredient = FluidStack.EMPTY;
		recipe.result = ItemStack.EMPTY;
		recipe.remain = out;
		recipe.step = step;
	}

	public BasinRecipeBuilder(FluidStack in, ItemStack out, int time) {
		super(CuisineRecipes.RS_BASIN.get());
		recipe.item_ingredients = Ingredient.EMPTY;
		recipe.fluid_ingredient = in;
		recipe.result = out;
		recipe.remain = FluidStack.EMPTY;
		recipe.time = time;
	}

	public Item getResult() {
		return this.recipe.result.getItem();
	}

	public void save(Consumer<FinishedRecipe> pvd) {
		save(pvd, new ResourceLocation(getResult().getRegistryName().getNamespace(), "basin/" + getResult().getRegistryName().getPath()));
	}

}
