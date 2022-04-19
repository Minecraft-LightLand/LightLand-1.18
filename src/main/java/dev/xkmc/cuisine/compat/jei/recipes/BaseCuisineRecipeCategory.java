package dev.xkmc.cuisine.compat.jei.recipes;

import com.tterrag.registrate.util.entry.BlockEntry;
import dev.hikarishima.lightland.compat.jei.recipes.BaseRecipeCategory;
import dev.xkmc.cuisine.init.Cuisine;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.forge.ForgeTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.fluids.FluidStack;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public abstract class BaseCuisineRecipeCategory<T, C extends BaseCuisineRecipeCategory<T, C>> extends BaseRecipeCategory<T, C> {

	protected final BlockEntry<?> icon_item;
	private final int max_fluid;

	public BaseCuisineRecipeCategory(String name, BlockEntry<?> icon_item, Class<T> cls, int max_fluid) {
		super(new ResourceLocation(Cuisine.MODID, name), cls);
		this.icon_item = icon_item;
		this.max_fluid = max_fluid;
	}

	public C init(IGuiHelper guiHelper) {
		background = guiHelper.createDrawable(BG, 0, 126, 162, 18);
		icon = guiHelper.createDrawableIngredient(VanillaTypes.ITEM_STACK, icon_item.asStack(1));
		return getThis();
	}

	@Override
	public Component getTitle() {
		return new TranslatableComponent(icon_item.get().getDescriptionId());
	}

	@Override
	public abstract void setRecipe(IRecipeLayoutBuilder builder, T recipe, IFocusGroup focuses);

	protected void addInputItem(IRecipeLayoutBuilder builder, int index, Ingredient ingredient) {
		builder.addSlot(RecipeIngredientRole.INPUT, index * 18 + 1, 1).addIngredients(ingredient);
	}

	protected void addInputFluid(IRecipeLayoutBuilder builder, int index, FluidStack ingredient) {
		builder.addSlot(RecipeIngredientRole.INPUT, index * 18 + 1, 1)
				.setFluidRenderer(max_fluid, true, 16, 16)
				.addIngredient(ForgeTypes.FLUID_STACK, ingredient);
	}

	protected void addOutputItem(IRecipeLayoutBuilder builder, int index, ItemStack ingredient) {
		builder.addSlot(RecipeIngredientRole.OUTPUT, 126 + index * 18 + 1, 1)
				.addIngredient(VanillaTypes.ITEM_STACK, ingredient);
	}

	protected void addOutputFluid(IRecipeLayoutBuilder builder, int index, FluidStack ingredient) {
		builder.addSlot(RecipeIngredientRole.OUTPUT, 126 + index * 18 + 1, 1)
				.setFluidRenderer(max_fluid, true, 16, 16)
				.addIngredient(ForgeTypes.FLUID_STACK, ingredient);
	}

}