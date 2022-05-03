package dev.xkmc.cuisine.content.tools.basin;

import dev.xkmc.l2library.recipe.BaseRecipeBuilder;
import dev.xkmc.cuisine.content.tools.base.RecipeContainer;
import dev.xkmc.cuisine.init.registrate.CuisineRecipes;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import java.util.function.Consumer;

public class BasinDryRecipeBuilder extends BaseRecipeBuilder<BasinDryRecipeBuilder, BasinDryRecipe, BasinDryRecipe, RecipeContainer<BasinBlockEntity>> {

	public BasinDryRecipeBuilder(FluidStack in, ItemStack out, int time) {
		super(CuisineRecipes.RS_BASIN_DRY.get());
		recipe.fluid_ingredient = in;
		recipe.result = out;
		recipe.time = time;
	}

	public Item getResult() {
		return this.recipe.result.getItem();
	}

	public void save(Consumer<FinishedRecipe> pvd) {
		save(pvd, new ResourceLocation(getResult().getRegistryName().getNamespace(), "basin_dry/" + getResult().getRegistryName().getPath()));
	}

}
