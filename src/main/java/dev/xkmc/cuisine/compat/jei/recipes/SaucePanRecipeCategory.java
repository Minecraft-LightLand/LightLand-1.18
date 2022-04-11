package dev.xkmc.cuisine.compat.jei.recipes;

import dev.xkmc.cuisine.content.tools.pan.PanBlockEntity;
import dev.xkmc.cuisine.content.tools.pan.SaucePanRecipe;
import dev.xkmc.cuisine.init.Cuisine;
import dev.xkmc.cuisine.init.registrate.CuisineBlocks;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IGuiIngredientGroup;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.plugins.vanilla.ingredients.fluid.FluidStackRenderer;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class SaucePanRecipeCategory implements IRecipeCategory<SaucePanRecipe> {

	private static final ResourceLocation BG = new ResourceLocation(Cuisine.MODID, "textures/jei/background.png");

	private static final FluidStackRenderer RENDERER = new FluidStackRenderer(PanBlockEntity.MAX_FLUID,
			true, 16, 16, null);

	private final ResourceLocation id;
	private IDrawable background, icon;

	public SaucePanRecipeCategory() {
		this.id = new ResourceLocation(Cuisine.MODID, "saucepan");
	}

	public SaucePanRecipeCategory init(IGuiHelper guiHelper) {
		background = guiHelper.createDrawable(BG, 0, 90, 162, 36);
		icon = guiHelper.createDrawableIngredient(CuisineBlocks.SAUCEPAN.get().asItem().getDefaultInstance());
		return this;
	}

	@Override
	public ResourceLocation getUid() {
		return id;
	}

	@SuppressWarnings({"unchecked", "rawtypes"})
	@Override
	public Class getRecipeClass() {
		return SaucePanRecipe.class;
	}

	@Override
	public Component getTitle() {
		return new TranslatableComponent(CuisineBlocks.SAUCEPAN.get().getDescriptionId());
	}

	@Override
	public IDrawable getBackground() {
		return background;
	}

	@Override
	public IDrawable getIcon() {
		return icon;
	}

	@Override
	public void setIngredients(SaucePanRecipe sl, IIngredients list) {
		list.setInputIngredients(sl.item_ingredients);
		list.setInputs(VanillaTypes.FLUID, sl.fluid_ingredients);
		list.setOutput(VanillaTypes.ITEM, sl.result);
	}

	@Override
	public void setRecipe(IRecipeLayout layout, SaucePanRecipe sl, IIngredients list) {
		setItem(layout.getItemStacks(), list.getOutputs(VanillaTypes.ITEM).get(0), 0, false, 144, 9);
		int index = 0;
		List<List<ItemStack>> items = list.getInputs(VanillaTypes.ITEM);
		for (int i = 0; i < items.size(); i++) {
			setItem(layout.getItemStacks(), items.get(i), i + 1, true, index % 6 * 18, index / 6 * 18);
			index++;
		}
		List<List<FluidStack>> fluids = list.getInputs(VanillaTypes.FLUID);
		for (int i = 0; i < fluids.size(); i++) {
			setFluid(layout.getFluidStacks(), fluids.get(i), i + 1, true, index % 6 * 18, index / 6 * 18);
			index++;
		}
	}

	private static void setItem(IGuiIngredientGroup<ItemStack> group, List<ItemStack> t, int ind, boolean bool, int x, int y) {
		group.init(ind, bool, x, y);
		group.set(ind, t);
	}

	private static void setFluid(IGuiIngredientGroup<FluidStack> group, List<FluidStack> t, int ind, boolean bool, int x, int y) {
		group.init(ind, bool, RENDERER, x, y, 16, 16, 1, 1);
		group.set(ind, t);
	}

}
