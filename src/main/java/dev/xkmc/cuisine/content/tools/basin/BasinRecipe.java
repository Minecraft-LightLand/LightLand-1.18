package dev.xkmc.cuisine.content.tools.basin;

import dev.lcy0x1.recipe.BaseRecipe;
import dev.lcy0x1.serial.SerialClass;
import dev.xkmc.cuisine.init.registrate.CuisineRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;

@SerialClass
public class BasinRecipe extends BaseRecipe<BasinRecipe, BasinRecipe, BasinBlockEntity.RecipeContainer> {

	@SerialClass.SerialField
	public Ingredient item_ingredients;
	@SerialClass.SerialField
	public ItemStack result;
	@SerialClass.SerialField
	public FluidStack fluid_ingredient, remain;
	@SerialClass.SerialField
	public int step, time;

	public BasinRecipe(ResourceLocation id) {
		super(id, CuisineRecipe.RS_BASIN.get());
	}

	@Override
	public boolean matches(BasinBlockEntity.RecipeContainer inv, Level world) {
		if (step > 0) { // item -> fluid
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
		} else { // fluid -> item
			FluidStack stack = inv.getTile().fluids.getFluidInTank(0);
			boolean match = !stack.isEmpty() && stack.isFluidEqual(fluid_ingredient) && stack.getAmount() >= fluid_ingredient.getAmount();
			if (!match)
				return false;
			return inv.getTile().inventory.canAddItem(result);
		}

	}

	@Override
	public ItemStack assemble(BasinBlockEntity.RecipeContainer inv) {
		if (step > 0) { // item -> fluid
			for (ItemStack stack : inv.getTile().inventory.getAsList()) {
				if (item_ingredients.test(stack)) {
					stack.shrink(1);
					break;
				}
			}
			inv.getTile().fluids.fill(remain.copy(), IFluidHandler.FluidAction.EXECUTE);
		} else { // fluid -> item
			inv.getTile().fluids.drain(fluid_ingredient.copy(), IFluidHandler.FluidAction.EXECUTE);
			inv.getTile().inventory.addItem(result.copy());
		}
		return ItemStack.EMPTY;
	}

	@Override
	public boolean canCraftInDimensions(int r, int c) {
		return false;
	}

	@Override
	public ItemStack getResultItem() {
		return result;
	}

}
