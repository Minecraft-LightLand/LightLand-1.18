package dev.xkmc.cuisine.content.tools.base;

import net.minecraft.world.item.BowlFoodItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;

import javax.annotation.Nonnull;

public class CuisineUtil {

	@Nonnull
	public static Ingredient getContainer(ItemStack result) {
		ItemStack container = result.getItem().getContainerItem(result);
		if (!container.isEmpty())
			return Ingredient.of(container);
		if (result.getItem() instanceof BowlFoodItem) {
			return Ingredient.of(Items.BOWL);
		}
		return Ingredient.EMPTY;
	}
}
