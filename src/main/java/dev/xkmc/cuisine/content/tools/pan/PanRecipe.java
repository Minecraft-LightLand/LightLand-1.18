package dev.xkmc.cuisine.content.tools.pan;

import dev.lcy0x1.recipe.BaseRecipe;
import dev.lcy0x1.serial.SerialClass;
import dev.xkmc.cuisine.init.registrate.CuisineRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraftforge.fluids.FluidStack;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@SerialClass
public class PanRecipe extends BaseRecipe<PanRecipe, PanRecipe, PanBlockEntity.RecipeContainer> {

	@SerialClass.SerialField
	public ArrayList<Ingredient> item_ingredients;
	@SerialClass.SerialField
	public ArrayList<FluidStack> fluid_ingredients;
	@SerialClass.SerialField
	public ItemStack result;
	@SerialClass.SerialField
	public int time;

	public PanRecipe(ResourceLocation id) {
		super(id, CuisineRecipe.RS_PAN.get());
	}

	@Override
	public boolean matches(PanBlockEntity.RecipeContainer inv, Level world) {
		List<Ingredient> items = new ArrayList<>(item_ingredients);
		for (ItemStack stack : inv.getTile().inputInventory.getAsList()) {
			if (stack.isEmpty())
				continue;
			if (items.isEmpty())
				return false;
			boolean match = false;
			for (Iterator<Ingredient> itr = items.iterator(); itr.hasNext(); ) {
				Ingredient ing = itr.next();
				if (ing.test(stack)) {
					itr.remove();
					match = true;
					break;
				}
			}
			if (!match)
				return false;
		}
		if (items.size() > 0) return false;
		List<FluidStack> fluids = new ArrayList<>(fluid_ingredients);
		for (FluidStack stack : inv.getTile().fluids.getAsList()) {
			if (stack.isEmpty())
				continue;
			if (fluids.isEmpty())
				return false;
			boolean match = false;
			for (Iterator<FluidStack> itr = fluids.iterator(); itr.hasNext(); ) {
				FluidStack ing = itr.next();
				if (ing.isFluidEqual(stack) && stack.getAmount() >= ing.getAmount()) {
					itr.remove();
					match = true;
					break;
				}
			}
			if (!match)
				return false;
		}
		return fluids.size() <= 0;
	}

	@Override
	public ItemStack assemble(PanBlockEntity.RecipeContainer inv) {
		return result.copy();
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
