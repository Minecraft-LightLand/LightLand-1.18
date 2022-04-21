package dev.xkmc.cuisine.content.tools.base;

import dev.xkmc.cuisine.content.tools.base.tile.CuisineTile;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.fluids.FluidStack;

import java.util.Iterator;
import java.util.List;
import java.util.function.BiPredicate;

public abstract class BaseCookingRecipe<R extends BaseCookingRecipe<R, T>, T extends CuisineTile<T>> extends CuisineRecipe<R, T> {

	private static final BiPredicate<Ingredient, ItemStack> ITEM_TEST =
			(ing, stack) -> !stack.isEmpty() && ing.test(stack);
	private static final BiPredicate<FluidStack, FluidStack> FLUID_TEST =
			(ing, stack) -> ing.isFluidEqual(stack) && stack.getAmount() >= ing.getAmount();
	private static final BiPredicate<Ingredient, ItemStack> ITEM_CONSUME =
			(ing, stack) -> {
				stack.shrink(1);
				return stack.isEmpty();
			};
	private static final BiPredicate<FluidStack, FluidStack> FLUID_CONSUME =
			(ing, stack) -> {
				stack.shrink(ing.getAmount());
				return stack.isEmpty();
			};


	private static <T, I> boolean fastReject(List<I> pred, List<T> inv, BiPredicate<I, T> func) {
		for (I ing : pred) {
			boolean match = false;
			for (T stack : inv) {
				if (func.test(ing, stack)) {
					match = true;
					break;
				}
			}
			if (!match)
				return true;
		}
		return false;
	}

	private static <T, I> boolean listFilter(List<I> pred, List<T> inv, BiPredicate<I, T> func, BiPredicate<I, T> isEmpty) {
		for (Iterator<I> ingitr = pred.iterator(); ingitr.hasNext(); ) {
			I ing = ingitr.next();
			boolean match = false;
			for (Iterator<T> stackitr = inv.iterator(); stackitr.hasNext(); ) {
				T stack = stackitr.next();
				if (func.test(ing, stack)) {
					if (isEmpty.test(ing, stack)) {
						stackitr.remove();
					}
					ingitr.remove();
					match = true;
					break;
				}
			}
			if (!match) {
				return true;
			}
		}
		return false;
	}

	public static boolean fastRejectItem(List<Ingredient> pred, List<ItemStack> inv) {
		return fastReject(pred, inv, ITEM_TEST);
	}

	public static boolean listFilterItem(List<Ingredient> pred, List<ItemStack> inv) {
		return listFilter(pred, inv, ITEM_TEST, ITEM_CONSUME);
	}

	public static boolean fastRejectFluid(List<FluidStack> pred, List<FluidStack> inv) {
		return fastReject(pred, inv, FLUID_TEST);
	}

	public static boolean listFilterFluid(List<FluidStack> pred, List<FluidStack> inv) {
		return listFilter(pred, inv, FLUID_TEST, FLUID_CONSUME);
	}

	public BaseCookingRecipe(ResourceLocation id, RecType<R, R, RecipeContainer<T>> fac) {
		super(id, fac);
	}

}
