package dev.xkmc.cuisine.compat.jei.recipes;

import dev.xkmc.cuisine.content.tools.firepit.FirePitStickRecipe;
import dev.xkmc.cuisine.content.tools.pan.PanBlockEntity;
import dev.xkmc.cuisine.content.tools.pan.PanRecipe;
import dev.xkmc.cuisine.init.registrate.CuisineBlocks;
import dev.xkmc.cuisine.init.registrate.CuisineRecipes;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.forge.ForgeTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.fluids.FluidStack;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class FirePitStickRecipeCategory extends BaseCuisineRecipeCategory<FirePitStickRecipe, FirePitStickRecipeCategory> {

	public FirePitStickRecipeCategory() {
		super("stick", CuisineBlocks.FIRE_PIT_STICK, FirePitStickRecipe.class, 0, CuisineRecipes.RT_STICK);
	}

	@Override
	public void setRecipe(IRecipeLayoutBuilder builder, FirePitStickRecipe recipe, IFocusGroup focuses) {
		int index = 0;
		for (Ingredient ingredient : recipe.ingredients) {
			builder.addSlot(RecipeIngredientRole.INPUT, index % 6 * 18 + 1, index / 6 * 18 + 1)
					.addIngredients(ingredient);
			index++;
		}
		if (!recipe.result.isEmpty())
			builder.addSlot(RecipeIngredientRole.OUTPUT, 145, 10)
					.addIngredient(VanillaTypes.ITEM_STACK, recipe.result);
	}

}
