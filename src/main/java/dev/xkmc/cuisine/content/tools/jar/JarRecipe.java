package dev.xkmc.cuisine.content.tools.jar;

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
import net.minecraftforge.fluids.capability.IFluidHandler;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@SerialClass
public class JarRecipe extends CuisineRecipe<JarRecipe, JarBlockEntity> implements TimeHandler.TimeRecipe {

	@SerialClass.SerialField
	public ArrayList<Ingredient> item_ingredients;
	@SerialClass.SerialField
	public ItemStack result;
	@SerialClass.SerialField
	public FluidStack fluid_ingredient, remain;
	@SerialClass.SerialField
	public int time;

	public JarRecipe(ResourceLocation id) {
		super(id, CuisineRecipes.RS_JAR.get());
	}

	@Override
	public boolean matches(RecipeContainer<JarBlockEntity> inv, Level world) {
		List<Ingredient> items = new ArrayList<>(item_ingredients);
		for (ItemStack stack : inv.getTile().inventory.getAsList()) {
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
		return fluid_ingredient.isFluidStackIdentical(inv.getTile().fluids.getFluidInTank(0));
	}

	@Override
	public ItemStack assemble(RecipeContainer<JarBlockEntity> inv) {
		inv.clear();
		inv.getTile().fluids.clear();
		inv.addItem(result.copy());
		inv.getTile().fluids.fill(remain.copy(), IFluidHandler.FluidAction.EXECUTE);
		return result.copy();
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
