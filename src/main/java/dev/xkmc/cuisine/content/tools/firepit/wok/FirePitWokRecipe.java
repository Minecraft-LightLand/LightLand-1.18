package dev.xkmc.cuisine.content.tools.firepit.wok;

import dev.xkmc.l2library.serial.SerialClass;
import dev.xkmc.cuisine.content.tools.base.CuisineRecipe;
import dev.xkmc.cuisine.content.tools.base.RecipeContainer;
import dev.xkmc.cuisine.content.tools.base.handlers.TimeHandler;
import dev.xkmc.cuisine.init.registrate.CuisineRecipes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraftforge.fluids.FluidStack;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@SerialClass
public class FirePitWokRecipe extends CuisineRecipe<FirePitWokRecipe, FirePitWokBlockEntity> implements TimeHandler.TimeRecipe {

	@SerialClass.SerialField
	public ArrayList<Ingredient> item_ingredients;
	@SerialClass.SerialField
	public ArrayList<FluidStack> fluid_ingredients;
	@SerialClass.SerialField
	public ItemStack result;
	@SerialClass.SerialField
	public int time;

	public FirePitWokRecipe(ResourceLocation id) {
		super(id, CuisineRecipes.RS_WOK.get());
	}

	@Override
	public boolean matches(RecipeContainer<FirePitWokBlockEntity> inv, Level world) {
		List<Ingredient> items = new ArrayList<>(item_ingredients);
		for (ItemStack stack : inv.getTile().getMainInventory().getAsList()) {
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
	public ItemStack assemble(RecipeContainer<FirePitWokBlockEntity> inv) {
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

	@Override
	public int getTime() {
		return time;
	}
}
