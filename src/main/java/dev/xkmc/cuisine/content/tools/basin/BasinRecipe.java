package dev.xkmc.cuisine.content.tools.basin;

import dev.xkmc.l2library.serial.SerialClass;
import dev.xkmc.cuisine.content.tools.base.CuisineRecipe;
import dev.xkmc.cuisine.content.tools.base.RecipeContainer;
import dev.xkmc.cuisine.content.tools.base.handlers.StepHandler;
import dev.xkmc.cuisine.init.registrate.CuisineRecipes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;

@SerialClass
public class BasinRecipe extends CuisineRecipe<BasinRecipe, BasinBlockEntity> implements StepHandler.StepRecipe {

	@SerialClass.SerialField
	public Ingredient item_ingredients;
	@SerialClass.SerialField
	public FluidStack remain;
	@SerialClass.SerialField
	public int step;

	public BasinRecipe(ResourceLocation id) {
		super(id, CuisineRecipes.RS_BASIN.get());
	}

	@Override
	public boolean matches(RecipeContainer<BasinBlockEntity> inv, Level world) {
		boolean hasFire = inv.getTile().checkBlockBelow();
		if (!hasFire) {
			boolean match = false;
			for (ItemStack stack : inv.getTile().inventory.getAsList()) {
				if (item_ingredients.test(stack)) {
					match = true;
					break;
				}
			}
			if (!match)
				return false;
			return inv.getTile().fluids.fill(remain.copy(), IFluidHandler.FluidAction.SIMULATE) == remain.getAmount();
		}
		return false;
	}

	@Override
	public ItemStack assemble(RecipeContainer<BasinBlockEntity> inv) {
		for (ItemStack stack : inv.getTile().inventory.getAsList()) {
			if (item_ingredients.test(stack)) {
				stack.shrink(1);
				break;
			}
		}
		inv.getTile().fluids.fill(remain.copy(), IFluidHandler.FluidAction.EXECUTE);
		return ItemStack.EMPTY;
	}

	@Override
	public ItemStack getResultItem() {
		return ItemStack.EMPTY;
	}

	@Override
	public int getStep() {
		return step;
	}
}
