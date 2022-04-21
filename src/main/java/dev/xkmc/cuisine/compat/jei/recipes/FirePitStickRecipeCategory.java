package dev.xkmc.cuisine.compat.jei.recipes;

import dev.xkmc.cuisine.content.tools.firepit.stick.FirePitStickRecipe;
import dev.xkmc.cuisine.init.registrate.CuisineBlocks;
import dev.xkmc.cuisine.init.registrate.CuisineRecipes;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.world.item.crafting.Ingredient;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class FirePitStickRecipeCategory extends BaseCuisineRecipeCategory<FirePitStickRecipe, FirePitStickRecipeCategory> {

	public FirePitStickRecipeCategory() {
		super("stick", CuisineBlocks.FIRE_PIT_STICK, FirePitStickRecipe.class, 0, CuisineRecipes.RT_STICK.get());
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

	public FirePitStickRecipeCategory init(IGuiHelper guiHelper) {
		background = guiHelper.createDrawable(BG, 0, 90, 162, 36);
		icon = guiHelper.createDrawableIngredient(VanillaTypes.ITEM_STACK, icon_item.asStack(1));
		return this;
	}

}
