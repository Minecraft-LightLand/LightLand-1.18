package dev.hikarishima.lightland.content.common.recipe;

import dev.hikarishima.lightland.init.data.AllTags;
import dev.hikarishima.lightland.init.registrate.RecipeRegistrate;
import dev.lcy0x1.recipe.AbstractShapelessRecipe;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;

public class BackpackDyeRecipe extends AbstractShapelessRecipe<BackpackDyeRecipe> {

	public BackpackDyeRecipe(ResourceLocation rl, String group, ItemStack result, NonNullList<Ingredient> ingredients) {
		super(rl, group, result, ingredients);
	}

	@Override
	public ItemStack assemble(CraftingContainer container) {
		ItemStack bag = ItemStack.EMPTY;
		for (int i = 0; i < container.getContainerSize(); i++) {
			if (AllTags.AllItemTags.BACKPACKS.matches(container.getItem(i))) {
				bag = container.getItem(i);
			}
		}
		ItemStack stack = super.assemble(container);
		stack.setTag(bag.getTag());
		return stack;
	}

	@Override
	public RecipeSerializer<?> getSerializer() {
		return RecipeRegistrate.RSC_BAG_DYE.get();
	}
}
