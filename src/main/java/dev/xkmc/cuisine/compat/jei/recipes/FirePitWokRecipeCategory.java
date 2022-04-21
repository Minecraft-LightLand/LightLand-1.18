package dev.xkmc.cuisine.compat.jei.recipes;

import dev.xkmc.cuisine.content.tools.firepit.wok.FirePitWokRecipe;
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
public class FirePitWokRecipeCategory extends BaseCuisineRecipeCategory<FirePitWokRecipe, FirePitWokRecipeCategory> {

	public FirePitWokRecipeCategory() {
		super("wok", CuisineBlocks.FIRE_PIT_WOK, FirePitWokRecipe.class, 0, CuisineRecipes.RT_WOK.get());
	}

	@Override
	public void setRecipe(IRecipeLayoutBuilder builder, FirePitWokRecipe recipe, IFocusGroup focuses) {
		int index = 0;
		for (Ingredient ingredient : recipe.item_ingredients) {
			builder.addSlot(RecipeIngredientRole.INPUT, index % 6 * 18 + 1, index / 6 * 18 + 1)
					.addIngredients(ingredient);
			index++;
		}
		for (FluidStack ingredient : recipe.fluid_ingredients) {
			builder.addSlot(RecipeIngredientRole.INPUT, index % 6 * 18 + 1, index / 6 * 18 + 1)
					.addIngredient(ForgeTypes.FLUID_STACK, ingredient);
			index++;
		}
		if (!recipe.result.isEmpty())
			builder.addSlot(RecipeIngredientRole.OUTPUT, 145, 10)
					.addIngredient(VanillaTypes.ITEM_STACK, recipe.result);
	}

	public FirePitWokRecipeCategory init(IGuiHelper guiHelper) {
		background = guiHelper.createDrawable(BG, 0, 90, 162, 36);
		icon = guiHelper.createDrawableIngredient(VanillaTypes.ITEM_STACK, icon_item.asStack(1));
		return this;
	}

}
