package dev.xkmc.cuisine.compat.jei.recipes;

import dev.xkmc.l2library.repack.registrate.util.entry.BlockEntry;
import dev.xkmc.l2library.recipe.BaseRecipeCategory;
import dev.xkmc.l2library.util.Proxy;
import dev.xkmc.cuisine.content.tools.base.CuisineRecipe;
import dev.xkmc.cuisine.init.Cuisine;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.forge.ForgeTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.fluids.FluidStack;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public abstract class BaseCuisineRecipeCategory<T extends CuisineRecipe<T, ?>, C extends BaseCuisineRecipeCategory<T, C>> extends BaseRecipeCategory<T, C> {

	protected static final ResourceLocation BG = new ResourceLocation(Cuisine.MODID, "textures/jei/background.png");

	protected final BlockEntry<?> icon_item;
	private final int max_fluid;
	private final RecipeType<T> type;

	public BaseCuisineRecipeCategory(String name, BlockEntry<?> icon_item, Class<T> cls, int max_fluid, RecipeType<T> type) {
		super(new ResourceLocation(Cuisine.MODID, name), cls);
		this.icon_item = icon_item;
		this.max_fluid = max_fluid;
		this.type = type;
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

	public void registerRecipes(IRecipeRegistration registration) {
		registration.addRecipes(getRecipeType(), Proxy.getWorld().getRecipeManager().getAllRecipesFor(type));
	}

	public void registerBlocks(IRecipeCatalystRegistration registration) {
		registration.addRecipeCatalyst(icon_item.get().asItem().getDefaultInstance(), getRecipeType());
	}

}
