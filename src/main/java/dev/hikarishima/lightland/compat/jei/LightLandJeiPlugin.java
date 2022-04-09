package dev.hikarishima.lightland.compat.jei;

import dev.hikarishima.lightland.compat.jei.ingredients.ElemIngredientHelper;
import dev.hikarishima.lightland.compat.jei.ingredients.ElemIngredientRenderer;
import dev.hikarishima.lightland.compat.jei.ingredients.ElementIngredient;
import dev.hikarishima.lightland.compat.jei.recipes.DisEnchanterRecipeCategory;
import dev.hikarishima.lightland.compat.jei.recipes.MagicCraftRecipeCategory;
import dev.hikarishima.lightland.compat.jei.recipes.SaucePanRecipeCategory;
import dev.hikarishima.lightland.compat.jei.screen.ExtraInfoScreen;
import dev.hikarishima.lightland.content.common.test.SaucePanRecipe;
import dev.hikarishima.lightland.content.magic.gui.craft.ArcaneInjectScreen;
import dev.hikarishima.lightland.content.magic.gui.craft.DisEnchanterScreen;
import dev.hikarishima.lightland.content.magic.gui.craft.SpellCraftScreen;
import dev.hikarishima.lightland.content.magic.products.recipe.IMagicRecipe;
import dev.hikarishima.lightland.init.LightLand;
import dev.hikarishima.lightland.init.registrate.BlockRegistrate;
import dev.hikarishima.lightland.init.registrate.ItemRegistrate;
import dev.hikarishima.lightland.init.registrate.RecipeRegistrate;
import dev.hikarishima.lightland.init.special.LightLandRegistry;
import dev.hikarishima.lightland.init.special.MagicRegistry;
import dev.lcy0x1.base.Proxy;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredientType;
import mezz.jei.api.registration.*;
import mezz.jei.api.runtime.IJeiRuntime;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.stream.Collectors;

@JeiPlugin
@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class LightLandJeiPlugin implements IModPlugin {

	public static LightLandJeiPlugin INSTANCE;

	public final ResourceLocation UID = new ResourceLocation(LightLand.MODID, "jei_plugin");

	public final DisEnchanterRecipeCategory DISENCHANT = new DisEnchanterRecipeCategory();
	public final MagicCraftRecipeCategory MAGIC_CRAFT = new MagicCraftRecipeCategory();
	public final SaucePanRecipeCategory SAUCEPAN = new SaucePanRecipeCategory();

	public final ElemIngredientHelper ELEM_HELPER = new ElemIngredientHelper();
	public final ElemIngredientRenderer ELEM_RENDERER = new ElemIngredientRenderer();
	public final IIngredientType<ElementIngredient> ELEM_TYPE = () -> ElementIngredient.class;

	public final ExtraInfoScreen EXTRA_INFO = new ExtraInfoScreen();

	public IGuiHelper GUI_HELPER;

	public LightLandJeiPlugin() {
		INSTANCE = this;
	}

	@Override
	public ResourceLocation getPluginUid() {
		return UID;
	}

	@Override
	public void registerItemSubtypes(ISubtypeRegistration registration) {

	}

	@Override
	public void registerIngredients(IModIngredientRegistration registration) {
		registration.register(ELEM_TYPE, LightLandRegistry.ELEMENT.getValues().stream()
						.map(ElementIngredient::new).collect(Collectors.toList()),
				ELEM_HELPER, ELEM_RENDERER);
	}

	@Override
	public void registerCategories(IRecipeCategoryRegistration registration) {
		IGuiHelper helper = registration.getJeiHelpers().getGuiHelper();
		registration.addRecipeCategories(DISENCHANT.init(helper));
		registration.addRecipeCategories(MAGIC_CRAFT.init(helper));
		registration.addRecipeCategories(SAUCEPAN.init(helper));
		GUI_HELPER = helper;
	}

	@Override
	public void registerVanillaCategoryExtensions(IVanillaCategoryExtensionRegistration registration) {
	}

	@Override
	public void registerRecipes(IRecipeRegistration registration) {
		registration.addRecipes(IMagicRecipe.getMap(Proxy.getWorld(), MagicRegistry.MPT_ENCH.get()).values(), DISENCHANT.getUid());
		registration.addRecipes(Proxy.getWorld().getRecipeManager().getAllRecipesFor(RecipeRegistrate.RT_RITUAL), MAGIC_CRAFT.getUid());
		registration.addRecipes(Proxy.getWorld().getRecipeManager().getAllRecipesFor(RecipeRegistrate.RT_PAN), SAUCEPAN.getUid());
	}

	@Override
	public void registerRecipeTransferHandlers(IRecipeTransferRegistration registration) {
	}

	@Override
	public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
		registration.addRecipeCatalyst(ItemRegistrate.DISENC_BOOK.get().getDefaultInstance(), DISENCHANT.getUid());
		registration.addRecipeCatalyst(BlockRegistrate.B_RITUAL_CORE.get().asItem().getDefaultInstance(), MAGIC_CRAFT.getUid());
		registration.addRecipeCatalyst(BlockRegistrate.SAUCEPAN.get().asItem().getDefaultInstance(), SAUCEPAN.getUid());
	}

	@Override
	public void registerGuiHandlers(IGuiHandlerRegistration registration) {
		ExtraInfoScreen.init();
		registration.addGuiContainerHandler(DisEnchanterScreen.class, EXTRA_INFO);
		registration.addGuiContainerHandler(SpellCraftScreen.class, EXTRA_INFO);
		registration.addGuiContainerHandler(ArcaneInjectScreen.class, EXTRA_INFO);
	}

	@Override
	public void registerAdvanced(IAdvancedRegistration registration) {
	}

	@Override
	public void onRuntimeAvailable(IJeiRuntime jeiRuntime) {
	}

}
