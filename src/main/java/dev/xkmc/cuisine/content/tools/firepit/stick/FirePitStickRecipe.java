package dev.xkmc.cuisine.content.tools.firepit.stick;

import dev.xkmc.l2library.serial.SerialClass;
import dev.xkmc.cuisine.content.tools.base.CuisineRecipe;
import dev.xkmc.cuisine.content.tools.base.RecipeContainer;
import dev.xkmc.cuisine.content.tools.base.handlers.TimeHandler;
import dev.xkmc.cuisine.init.registrate.CuisineRecipes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@SerialClass
public class FirePitStickRecipe extends CuisineRecipe<FirePitStickRecipe, FirePitStickBlockEntity> implements TimeHandler.TimeRecipe {

	@SerialClass.SerialField
	public List<Ingredient> ingredients;
	@SerialClass.SerialField
	public ItemStack result;
	@SerialClass.SerialField
	public int time;

	public FirePitStickRecipe(ResourceLocation id) {
		super(id, CuisineRecipes.RS_STICK.get());
	}

	@Override
	public boolean matches(RecipeContainer<FirePitStickBlockEntity> inv, Level world) {
		List<Ingredient> required = new ArrayList<>(ingredients);
		for (ItemStack stack : inv.getAsList()) {
			for (Iterator<Ingredient> itr = required.iterator(); itr.hasNext(); ) {
				Ingredient ing = itr.next();
				if (ing.test(stack)) {
					itr.remove();
					break;
				}
			}
		}
		return required.size() == 0;
	}

	@Override
	public ItemStack assemble(RecipeContainer<FirePitStickBlockEntity> inv) {
		List<Ingredient> required = new ArrayList<>(ingredients);
		for (ItemStack stack : inv.getAsList()) {
			for (Iterator<Ingredient> itr = required.iterator(); itr.hasNext(); ) {
				Ingredient ing = itr.next();
				if (ing.test(stack)) {
					itr.remove();
					stack.shrink(1);
					break;
				}
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
	public int getTime() {
		return time;
	}
}
