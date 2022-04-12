package dev.xkmc.cuisine.compat.jei.recipes;

import dev.xkmc.cuisine.content.tools.jar.JarBlockEntity;
import dev.xkmc.cuisine.content.tools.jar.JarRecipe;
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
public class JarRecipeCategory implements IRecipeCategory<JarRecipe> {

	private static final ResourceLocation BG = new ResourceLocation(Cuisine.MODID, "textures/jei/background.png");

	private static final FluidStackRenderer RENDERER = new FluidStackRenderer(JarBlockEntity.MAX_FLUID,
			true, 16, 16, null);

	private final ResourceLocation id;
	private IDrawable background, icon;

	public JarRecipeCategory() {
		this.id = new ResourceLocation(Cuisine.MODID, "jar");
	}

	public JarRecipeCategory init(IGuiHelper guiHelper) {
		background = guiHelper.createDrawable(BG, 0, 126, 162, 18);
		icon = guiHelper.createDrawableIngredient(CuisineBlocks.PAN.get().asItem().getDefaultInstance());
		return this;
	}

	@Override
	public ResourceLocation getUid() {
		return id;
	}

	@SuppressWarnings({"unchecked", "rawtypes"})
	@Override
	public Class getRecipeClass() {
		return JarRecipe.class;
	}

	@Override
	public Component getTitle() {
		return new TranslatableComponent(CuisineBlocks.JAR.get().getDescriptionId());
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
	public void setIngredients(JarRecipe sl, IIngredients list) {
		list.setInputIngredients(sl.item_ingredients);
		if (!sl.fluid_ingredient.isEmpty())
			list.setInputs(VanillaTypes.FLUID, List.of(sl.fluid_ingredient));
		if (!sl.result.isEmpty())
			list.setOutput(VanillaTypes.ITEM, sl.result);
		if (!sl.remain.isEmpty())
			list.setOutput(VanillaTypes.FLUID, sl.remain);
	}

	@Override
	public void setRecipe(IRecipeLayout layout, JarRecipe sl, IIngredients list) {
		int index = 0;
		List<List<ItemStack>> items = list.getInputs(VanillaTypes.ITEM);
		for (int i = 0; i < items.size(); i++) {
			setItem(layout.getItemStacks(), items.get(i), i + 1, true, index * 18, 0);
			index++;
		}
		List<List<FluidStack>> fluids = list.getInputs(VanillaTypes.FLUID);
		for (int i = 0; i < fluids.size(); i++) {
			setFluid(layout.getFluidStacks(), fluids.get(i), i + 1, true, index * 18, 0);
			index++;
		}
		index = 0;
		if (!sl.result.isEmpty())
			setItem(layout.getItemStacks(), List.of(sl.result), 0, false, 126 + (index++) * 18, 0);
		if (!sl.remain.isEmpty())
			setFluid(layout.getFluidStacks(), List.of(sl.remain), 0, false, 126 + (index++) * 18, 0);
	}

	private static void setItem(IGuiIngredientGroup<ItemStack> group, List<ItemStack> t, int ind, boolean bool, int x, int y) {
		group.init(ind, bool, x, y);
		group.set(ind, t);
	}

	private static void setFluid(IGuiIngredientGroup<FluidStack> group, List<FluidStack> t, int ind, boolean bool, int x, int y) {
		group.init(ind, bool, RENDERER, x + 1, y + 1, 16, 16, 0, 0);
		group.set(ind, t);
	}

}
