package dev.xkmc.cuisine.content.tools.basin;

import dev.lcy0x1.recipe.BaseRecipeBuilder;
import dev.xkmc.cuisine.content.tools.jar.JarBlockEntity;
import dev.xkmc.cuisine.init.registrate.CuisineRecipe;
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

public class BasinRecipeBuilder extends BaseRecipeBuilder<BasinRecipeBuilder, BasinRecipe, BasinRecipe, BasinBlockEntity.RecipeContainer> {

	public BasinRecipeBuilder(ItemLike result, int count, int step, FluidStack out) {
		super(CuisineRecipe.RS_BASIN.get());
		recipe.item_ingredients = new ArrayList<>();
		recipe.result = new ItemStack(result.asItem(), count);
		recipe.remain = out;
		recipe.step = step;
	}

	public BasinRecipeBuilder requires(TagKey<Item> tagKey) {
		return this.requires(Ingredient.of(tagKey));
	}

	public BasinRecipeBuilder requires(ItemLike itemLike) {
		return this.requires(itemLike, 1);
	}

	public BasinRecipeBuilder requires(ItemLike itemLike, int count) {
		for (int i = 0; i < count; ++i) {
			this.requires(Ingredient.of(itemLike));
		}

		return this;
	}

	public BasinRecipeBuilder requires(Ingredient ingredient) {
		return this.requires(ingredient, 1);
	}

	public BasinRecipeBuilder requires(Ingredient ingredient, int count) {
		for (int i = 0; i < count; ++i) {
			this.recipe.item_ingredients.add(ingredient);
		}
		return this;
	}

	public Item getResult() {
		return this.recipe.result.getItem();
	}

	public void save(Consumer<FinishedRecipe> pvd) {
		save(pvd, new ResourceLocation(getResult().getRegistryName().getNamespace(), "basin/" + getResult().getRegistryName().getPath()));
	}

}
