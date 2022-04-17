package dev.xkmc.cuisine.content.tools.mill;

import dev.lcy0x1.recipe.BaseRecipeBuilder;
import dev.xkmc.cuisine.init.registrate.CuisineRecipe;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.fluids.FluidStack;

import java.util.ArrayList;
import java.util.function.Consumer;

public class MillRecipeBuilder extends BaseRecipeBuilder<MillRecipeBuilder, MillRecipe, MillRecipe, MillBlockEntity.RecipeContainer> {

	public MillRecipeBuilder(Ingredient in, FluidStack out, int water, int step) {
		super(CuisineRecipe.RS_MILL.get());
		recipe.item_ingredients = in;
		recipe.fluid_ingredient = water == 0 ? FluidStack.EMPTY : new FluidStack(Fluids.WATER, water);
		recipe.remain = out;
		recipe.step = step;
	}

}
