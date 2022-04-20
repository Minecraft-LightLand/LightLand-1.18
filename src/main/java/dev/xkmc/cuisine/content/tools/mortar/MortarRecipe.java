package dev.xkmc.cuisine.content.tools.mortar;

import dev.lcy0x1.recipe.BaseRecipe;
import dev.lcy0x1.serial.SerialClass;
import dev.xkmc.cuisine.init.registrate.CuisineRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;

@SerialClass
public class MortarRecipe extends BaseRecipe<MortarRecipe, MortarRecipe, MortarBlockEntity.RecipeContainer> {

	@SerialClass.SerialField
	public Ingredient ingredient;
	@SerialClass.SerialField
	public ItemStack result;
	@SerialClass.SerialField
	public int step;

	public MortarRecipe(ResourceLocation id) {
		super(id, CuisineRecipe.RS_MORTAR.get());
	}

	@Override
	public boolean matches(MortarBlockEntity.RecipeContainer inv, Level world) {
		if (!inv.canAddItem(result.copy()))
			return false;
		for (ItemStack stack : inv.getAsList()) {
			if (ingredient.test(stack)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public ItemStack assemble(MortarBlockEntity.RecipeContainer inv) {
		for (ItemStack stack : inv.getAsList()) {
			if (ingredient.test(stack)) {
				stack.shrink(1);
				break;
			}
		}
		inv.addItem(result.copy());
		return ItemStack.EMPTY;
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
