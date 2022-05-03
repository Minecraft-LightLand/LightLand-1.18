package dev.xkmc.cuisine.content.tools.pan;

import dev.xkmc.l2library.serial.SerialClass;
import dev.xkmc.cuisine.content.tools.base.BaseCookingRecipe;
import dev.xkmc.cuisine.content.tools.base.RecipeContainer;
import dev.xkmc.cuisine.content.tools.base.handlers.TimeHandler;
import dev.xkmc.cuisine.init.data.CuisineTags;
import dev.xkmc.cuisine.init.registrate.CuisineRecipes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraftforge.fluids.FluidStack;

import java.util.ArrayList;
import java.util.List;

@SerialClass
public class PanRecipe extends BaseCookingRecipe<PanRecipe, PanBlockEntity> implements TimeHandler.TimeRecipe {

	@SerialClass.SerialField
	public ArrayList<Ingredient> item_ingredients;
	@SerialClass.SerialField
	public ArrayList<FluidStack> fluid_ingredients;
	@SerialClass.SerialField
	public ItemStack result;
	@SerialClass.SerialField
	public int time;

	public PanRecipe(ResourceLocation id) {
		super(id, CuisineRecipes.RS_PAN.get());
	}

	@Override
	public boolean matches(RecipeContainer<PanBlockEntity> inv, Level world) {
		if (fastRejectItem(item_ingredients, inv.getAsList()))
			return false;
		if (fastRejectFluid(fluid_ingredients, inv.getTile().fluids.getAsList()))
			return false;
		List<ItemStack> remain_item = new ArrayList<>();
		for (ItemStack stack : inv.getAsList()) {
			if (!stack.isEmpty())
				remain_item.add(stack.copy());
		}
		List<Ingredient> items = new ArrayList<>(item_ingredients);
		if (listFilterItem(items, remain_item))
			return false;
		List<FluidStack> remain_fluid = new ArrayList<>();
		for (FluidStack stack : inv.getTile().fluids.getAsList()) {
			if (!stack.isEmpty())
				remain_fluid.add(stack.copy());
		}
		List<FluidStack> fluids = new ArrayList<>(fluid_ingredients);
		if (listFilterFluid(fluids, remain_fluid))
			return false;
		for (ItemStack remain : remain_item) {
			if (CuisineTags.AllItemTags.CONDIMENT.matches(remain))
				continue;
			if (CuisineTags.AllItemTags.SIDE.matches(remain))
				continue;
			return false;
		}
		return true;
	}

	@Override
	public ItemStack assemble(RecipeContainer<PanBlockEntity> inv) {
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
