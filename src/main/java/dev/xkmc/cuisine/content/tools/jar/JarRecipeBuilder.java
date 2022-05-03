package dev.xkmc.cuisine.content.tools.jar;

import dev.xkmc.l2library.recipe.BaseRecipeBuilder;
import dev.xkmc.cuisine.content.tools.base.RecipeContainer;
import dev.xkmc.cuisine.init.registrate.CuisineRecipes;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.fluids.FluidStack;

import java.util.ArrayList;
import java.util.function.Consumer;

public class JarRecipeBuilder extends BaseRecipeBuilder<JarRecipeBuilder, JarRecipe, JarRecipe, RecipeContainer<JarBlockEntity>> {

	public JarRecipeBuilder(ItemLike result, int count, int time, FluidStack in, FluidStack out) {
		super(CuisineRecipes.RS_JAR.get());
		recipe.item_ingredients = new ArrayList<>();
		recipe.result = new ItemStack(result.asItem(), count);
		recipe.fluid_ingredient = in;
		recipe.remain = out;
		recipe.time = time;
	}

	public JarRecipeBuilder requires(TagKey<Item> tagKey) {
		return this.requires(Ingredient.of(tagKey));
	}

	public JarRecipeBuilder requires(ItemLike itemLike) {
		return this.requires(itemLike, 1);
	}

	public JarRecipeBuilder requires(ItemLike itemLike, int count) {
		for (int i = 0; i < count; ++i) {
			this.requires(Ingredient.of(itemLike));
		}

		return this;
	}

	public JarRecipeBuilder requires(Ingredient ingredient) {
		return this.requires(ingredient, 1);
	}

	public JarRecipeBuilder requires(Ingredient ingredient, int count) {
		for (int i = 0; i < count; ++i) {
			this.recipe.item_ingredients.add(ingredient);
		}
		return this;
	}

	public Item getResult() {
		return this.recipe.result.getItem();
	}

	public void save(Consumer<FinishedRecipe> pvd) {
		save(pvd, new ResourceLocation(getResult().getRegistryName().getNamespace(), "jar/" + getResult().getRegistryName().getPath()));
	}

}
