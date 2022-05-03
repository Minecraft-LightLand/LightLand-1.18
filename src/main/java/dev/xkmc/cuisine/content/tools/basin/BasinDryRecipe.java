package dev.xkmc.cuisine.content.tools.basin;

import dev.xkmc.l2library.serial.SerialClass;
import dev.xkmc.cuisine.content.tools.base.CuisineRecipe;
import dev.xkmc.cuisine.content.tools.base.RecipeContainer;
import dev.xkmc.cuisine.content.tools.base.handlers.TimeHandler;
import dev.xkmc.cuisine.init.registrate.CuisineRecipes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;

@SerialClass
public class BasinDryRecipe extends CuisineRecipe<BasinDryRecipe, BasinBlockEntity> implements TimeHandler.TimeRecipe {

	@SerialClass.SerialField
	public ItemStack result;
	@SerialClass.SerialField
	public FluidStack fluid_ingredient;
	@SerialClass.SerialField
	public int time;

	public BasinDryRecipe(ResourceLocation id) {
		super(id, CuisineRecipes.RS_BASIN_DRY.get());
	}

	@Override
	public boolean matches(RecipeContainer<BasinBlockEntity> inv, Level world) {
		boolean hasFire = inv.getTile().checkBlockBelow();
		if (hasFire && time > 0) { // fluid -> item
			FluidStack stack = inv.getTile().fluids.getFluidInTank(0);
			boolean match = !stack.isEmpty() && stack.isFluidEqual(fluid_ingredient) && stack.getAmount() >= fluid_ingredient.getAmount();
			if (!match)
				return false;
			return inv.getTile().inventory.canAddItem(result);
		}
		return false;
	}

	@Override
	public ItemStack assemble(RecipeContainer<BasinBlockEntity> inv) {
		inv.getTile().fluids.drain(fluid_ingredient.copy(), IFluidHandler.FluidAction.EXECUTE);
		inv.getTile().inventory.addItem(result.copy());
		return ItemStack.EMPTY;
	}

	@Override
	public ItemStack getResultItem() {
		return result;
	}

	@Override
	public int getTime() {
		return time;
	}
}
