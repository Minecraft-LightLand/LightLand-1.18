package dev.xkmc.cuisine.content.tools.mill;

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
public class MillRecipe extends CuisineRecipe<MillRecipe, MillBlockEntity> implements StepHandler.StepRecipe {

	@SerialClass.SerialField
	public Ingredient item_ingredients;
	@SerialClass.SerialField
	public FluidStack fluid_ingredient, remain;
	@SerialClass.SerialField
	public int step;

	public MillRecipe(ResourceLocation id) {
		super(id, CuisineRecipes.RS_MILL.get());
	}

	@Override
	public boolean matches(RecipeContainer<MillBlockEntity> inv, Level world) {
		if (!item_ingredients.test(inv.getTile().inventory.getItem(0)))
			return false;
		if (!fluid_ingredient.isFluidEqual(inv.getTile().water.drain(fluid_ingredient.copy(), IFluidHandler.FluidAction.SIMULATE)))
			return false;
		return inv.getTile().fluids.fill(remain.copy(), IFluidHandler.FluidAction.SIMULATE) == remain.getAmount();
	}

	@Override
	public ItemStack assemble(RecipeContainer<MillBlockEntity> inv) {
		inv.getTile().inventory.getItem(0).shrink(1);
		inv.getTile().water.drain(fluid_ingredient.copy(), IFluidHandler.FluidAction.EXECUTE);
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
