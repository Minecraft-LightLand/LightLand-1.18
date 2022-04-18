package dev.hikarishima.lightland.compat.jei.recipes;

import com.tterrag.registrate.util.entry.BlockEntry;
import dev.xkmc.cuisine.init.Cuisine;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.forge.ForgeTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
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
public abstract class BaseRecipeCategory<T, C extends BaseRecipeCategory<T, C>> implements IRecipeCategory<T> {

	protected static final ResourceLocation BG = new ResourceLocation(Cuisine.MODID, "textures/jei/background.png");

	@SuppressWarnings("unchecked")
	public static <T extends R, R> Class<T> cast(Class<R> cls){
		return (Class<T>) cls;
	}

	private final ResourceLocation id;
	private final Class<T> cls;
	private final RecipeType<T> type;

	protected IDrawable background, icon;

	public BaseRecipeCategory(ResourceLocation name, Class<T> cls) {
		this.id = name;
		this.cls = cls;
		this.type = new RecipeType<>(getUid(), getRecipeClass());
	}

	@SuppressWarnings("unchecked")
	public final C getThis() {
		return (C) this;
	}

	@Override
	public final RecipeType<T> getRecipeType() {
		return type;
	}

	@Override
	public final ResourceLocation getUid() {
		return id;
	}

	@Override
	public final Class<T> getRecipeClass() {
		return cls;
	}

	@Override
	public final IDrawable getBackground() {
		return background;
	}

	@Override
	public final IDrawable getIcon() {
		return icon;
	}

	@Override
	public abstract void setRecipe(IRecipeLayoutBuilder builder, T recipe, IFocusGroup focuses);

}
