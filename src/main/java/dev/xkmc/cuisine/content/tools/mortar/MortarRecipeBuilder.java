package dev.xkmc.cuisine.content.tools.mortar;

import dev.lcy0x1.recipe.BaseRecipeBuilder;
import dev.xkmc.cuisine.init.registrate.CuisineRecipe;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.function.Consumer;

public class MortarRecipeBuilder extends BaseRecipeBuilder<MortarRecipeBuilder, MortarRecipe, MortarRecipe, MortarBlockEntity.RecipeContainer> {

	public MortarRecipeBuilder(Ingredient in, ItemStack out, int step) {
		super(CuisineRecipe.RS_MORTAR.get());
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
