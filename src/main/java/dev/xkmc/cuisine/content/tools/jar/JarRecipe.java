package dev.xkmc.cuisine.content.tools.jar;

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
public class JarRecipe extends BaseRecipe<JarRecipe, JarRecipe, JarBlockEntity.RecipeContainer> {

	@SerialClass.SerialField
	public ArrayList<Ingredient> item_ingredients;
	@SerialClass.SerialField
	public ItemStack result;
	@SerialClass.SerialField
	public FluidStack fluid_ingredient, remain;
	@SerialClass.SerialField
	public int time;

	public JarRecipe(ResourceLocation id) {
		super(id, CuisineRecipe.RS_JAR.get());
	}

	@Override
	public boolean matches(JarBlockEntity.RecipeContainer inv, Level world) {
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
	public ItemStack assemble(JarBlockEntity.RecipeContainer inv) {
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
