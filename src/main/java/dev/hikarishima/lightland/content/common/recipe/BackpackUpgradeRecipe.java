package dev.hikarishima.lightland.content.common.recipe;

import dev.hikarishima.lightland.init.registrate.LightlandRecipe;
import dev.xkmc.l2library.recipe.AbstractSmithingRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;

public class BackpackUpgradeRecipe extends AbstractSmithingRecipe<BackpackUpgradeRecipe> {

	public BackpackUpgradeRecipe(ResourceLocation rl, Ingredient left, Ingredient right, ItemStack result) {
		super(rl, left, right, result);
	}

	@Override
	public boolean matches(Container container, Level level) {
		if (!super.matches(container, level)) return false;
		return container.getItem(0).getOrCreateTag().getInt("rows") < 6;
	}

	@Override
	public ItemStack assemble(Container container) {
		ItemStack stack = super.assemble(container);
		stack.getOrCreateTag().putInt("rows", Math.max(1, stack.getOrCreateTag().getInt("rows")) + 1);
		return stack;
	}

	@Override
	public RecipeSerializer<?> getSerializer() {
		return LightlandRecipe.RSC_BAG_UPGRADE.get();
	}
}
