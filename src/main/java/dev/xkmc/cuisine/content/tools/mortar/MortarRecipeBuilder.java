package dev.xkmc.cuisine.content.tools.mortar;

import dev.xkmc.l2library.recipe.BaseRecipeBuilder;
import dev.xkmc.cuisine.content.tools.base.RecipeContainer;
import dev.xkmc.cuisine.init.registrate.CuisineRecipes;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.function.Consumer;

public class MortarRecipeBuilder extends BaseRecipeBuilder<MortarRecipeBuilder, MortarRecipe, MortarRecipe, RecipeContainer<MortarBlockEntity>> {

	public MortarRecipeBuilder(Ingredient in, ItemStack out, int step) {
		super(CuisineRecipes.RS_MORTAR.get());
		recipe.ingredient = in;
		recipe.result = out;
		recipe.step = step;
	}

	public Item getResult() {
		return this.recipe.result.getItem();
	}

	public void save(Consumer<FinishedRecipe> pvd) {
		save(pvd, new ResourceLocation(getResult().getRegistryName().getNamespace(), "mortar/" + getResult().getRegistryName().getPath()));
	}

}
