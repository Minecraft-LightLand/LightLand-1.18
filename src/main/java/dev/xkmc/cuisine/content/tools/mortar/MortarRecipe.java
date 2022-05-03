package dev.xkmc.cuisine.content.tools.mortar;

import dev.xkmc.l2library.serial.SerialClass;
import dev.xkmc.cuisine.content.tools.base.CuisineRecipe;
import dev.xkmc.cuisine.content.tools.base.RecipeContainer;
import dev.xkmc.cuisine.content.tools.base.handlers.StepHandler;
import dev.xkmc.cuisine.init.registrate.CuisineRecipes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;

@SerialClass
public class MortarRecipe extends CuisineRecipe<MortarRecipe, MortarBlockEntity> implements StepHandler.StepRecipe {

	@SerialClass.SerialField
	public Ingredient ingredient;
	@SerialClass.SerialField
	public ItemStack result;
	@SerialClass.SerialField
	public int step;

	public MortarRecipe(ResourceLocation id) {
		super(id, CuisineRecipes.RS_MORTAR.get());
	}

	@Override
	public boolean matches(RecipeContainer<MortarBlockEntity> inv, Level world) {
		if (!inv.canAddItem(result.copy()))
			return false;
		for (ItemStack stack : inv.getAsList()) {
			if (ingredient.test(stack)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public ItemStack assemble(RecipeContainer<MortarBlockEntity> inv) {
		for (ItemStack stack : inv.getAsList()) {
			if (ingredient.test(stack)) {
				stack.shrink(1);
				break;
			}
		}
		inv.addItem(result.copy());
		return ItemStack.EMPTY;
	}

	@Override
	public ItemStack getResultItem() {
		return result;
	}

	@Override
	public int getStep() {
		return step;
	}
}
