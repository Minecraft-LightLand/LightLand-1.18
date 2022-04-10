package dev.hikarishima.lightland.content.secondary.pan;

import dev.hikarishima.lightland.init.registrate.RecipeRegistrate;
import dev.lcy0x1.recipe.BaseRecipeBuilder;
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

public class SaucePanRecipeBuilder extends BaseRecipeBuilder<SaucePanRecipeBuilder, SaucePanRecipe, SaucePanRecipe, PanBlockEntity.RecipeContainer> {

	public SaucePanRecipeBuilder(ItemLike result, int count, int time, Item interrupt) {
		super(RecipeRegistrate.RS_PAN.get());
		recipe.item_ingredients = new ArrayList<>();
		recipe.fluid_ingredients = new ArrayList<>();
		recipe.result = new ItemStack(result.asItem(), count);
		recipe.interrupt = new ItemStack(interrupt);
		recipe.time = time;
	}

	public SaucePanRecipeBuilder requires(TagKey<Item> tagKey) {
		return this.requires(Ingredient.of(tagKey));
	}

	public SaucePanRecipeBuilder requires(ItemLike itemLike) {
		return this.requires(itemLike, 1);
	}

	public SaucePanRecipeBuilder requires(ItemLike itemLike, int count) {
		for (int i = 0; i < count; ++i) {
			this.requires(Ingredient.of(itemLike));
		}

		return this;
	}

	public SaucePanRecipeBuilder requires(Ingredient ingredient) {
		return this.requires(ingredient, 1);
	}

	public SaucePanRecipeBuilder requires(FluidStack stack) {
		this.recipe.fluid_ingredients.add(stack);
		return this;
	}

	public SaucePanRecipeBuilder requires(Ingredient ingredient, int count) {
		for (int i = 0; i < count; ++i) {
			this.recipe.item_ingredients.add(ingredient);
		}
		return this;
	}

	public Item getResult() {
		return this.recipe.result.getItem();
	}

	public void save(Consumer<FinishedRecipe> pvd) {
		save(pvd, new ResourceLocation(getResult().getRegistryName().getNamespace(), "saucepan/" + getResult().getRegistryName().getPath()));
	}

}
