package dev.xkmc.cuisine.compat.jei;

import dev.hikarishima.lightland.compat.jei.screen.ExtraInfoScreen;
import dev.hikarishima.lightland.init.LightLand;
import dev.lcy0x1.base.Proxy;
import dev.xkmc.cuisine.compat.jei.recipes.JarRecipeCategory;
import dev.xkmc.cuisine.compat.jei.recipes.SaucePanRecipeCategory;
import dev.xkmc.cuisine.init.registrate.CuisineBlocks;
import dev.xkmc.cuisine.init.registrate.CuisineRecipe;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.registration.*;
import mezz.jei.api.runtime.IJeiRuntime;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.ParametersAreNonnullByDefault;

@JeiPlugin
@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class CuisineJeiPlugin implements IModPlugin {

	public static CuisineJeiPlugin INSTANCE;

	public final ResourceLocation UID = new ResourceLocation(LightLand.MODID, "cuisine");

	public final SaucePanRecipeCategory PAN = new SaucePanRecipeCategory();
	public final JarRecipeCategory JAR = new JarRecipeCategory();

	public IGuiHelper GUI_HELPER;

	public CuisineJeiPlugin() {
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
	}

	@Override
	public void registerCategories(IRecipeCategoryRegistration registration) {
		IGuiHelper helper = registration.getJeiHelpers().getGuiHelper();
		registration.addRecipeCategories(PAN.init(helper));
		registration.addRecipeCategories(JAR.init(helper));
		GUI_HELPER = helper;
	}

	@Override
	public void registerVanillaCategoryExtensions(IVanillaCategoryExtensionRegistration registration) {
	}

	@Override
	public void registerRecipes(IRecipeRegistration registration) {
		registration.addRecipes(Proxy.getWorld().getRecipeManager().getAllRecipesFor(CuisineRecipe.RT_PAN), PAN.getUid());
		registration.addRecipes(Proxy.getWorld().getRecipeManager().getAllRecipesFor(CuisineRecipe.RT_JAR), JAR.getUid());
	}

	@Override
	public void registerRecipeTransferHandlers(IRecipeTransferRegistration registration) {
	}

	@Override
	public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
		registration.addRecipeCatalyst(CuisineBlocks.SAUCEPAN.get().asItem().getDefaultInstance(), PAN.getUid());
		registration.addRecipeCatalyst(CuisineBlocks.JAR.get().asItem().getDefaultInstance(), JAR.getUid());
	}

	@Override
	public void registerGuiHandlers(IGuiHandlerRegistration registration) {
		ExtraInfoScreen.init();
	}

	@Override
	public void registerAdvanced(IAdvancedRegistration registration) {
	}

	@Override
	public void onRuntimeAvailable(IJeiRuntime jeiRuntime) {
	}

}
