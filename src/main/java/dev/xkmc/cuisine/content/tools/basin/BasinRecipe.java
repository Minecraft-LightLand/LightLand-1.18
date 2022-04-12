package dev.xkmc.cuisine.content.tools.basin;

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
public class BasinRecipe extends BaseRecipe<BasinRecipe, BasinRecipe, BasinBlockEntity.RecipeContainer> {

	@SerialClass.SerialField
	public ArrayList<Ingredient> item_ingredients;
	@SerialClass.SerialField
	public ItemStack result;
	@SerialClass.SerialField
	public FluidStack remain;
	@SerialClass.SerialField
	public int step;

	public BasinRecipe(ResourceLocation id) {
		super(id, CuisineRecipe.RS_BASIN.get());
	}

	@Override
	public boolean matches(BasinBlockEntity.RecipeContainer inv, Level world) {
		List<Ingredient> items = new ArrayList<>(item_ingredients);
		for (ItemStack stack : inv.getTile().inventory.getAsList()) {
			if (stack.isEmpty())
				continue;
			if (items.isEmpty())
				return true;
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
		return items.size() <= 0;
	}

	@Override
	public ItemStack assemble(BasinBlockEntity.RecipeContainer inv) {
		List<Ingredient> items = new ArrayList<>(item_ingredients);
		for (ItemStack stack : inv.getTile().inventory.getAsList()) {
			if (stack.isEmpty())
				continue;
			if (items.isEmpty())
				break;
			for (Iterator<Ingredient> itr = items.iterator(); itr.hasNext(); ) {
				Ingredient ing = itr.next();
				if (ing.test(stack)) {
					itr.remove();
					stack.shrink(1);
					break;
				}
			}
		}
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
