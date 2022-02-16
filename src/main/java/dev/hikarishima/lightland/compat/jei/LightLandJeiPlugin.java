package dev.hikarishima.lightland.compat.jei;

import dev.hikarishima.lightland.compat.jei.recipes.MagicCraftRecipeCategory;
import dev.hikarishima.lightland.init.LightLand;
import dev.hikarishima.lightland.init.registrate.BlockRegistrate;
import dev.hikarishima.lightland.init.registrate.RecipeRegistrate;
import dev.lcy0x1.base.Proxy;
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
public class LightLandJeiPlugin implements IModPlugin {

    public static LightLandJeiPlugin INSTANCE;

    public final ResourceLocation UID = new ResourceLocation(LightLand.MODID, "jei_plugin");

    public final MagicCraftRecipeCategory MAGIC_CRAFT = new MagicCraftRecipeCategory();

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
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        IGuiHelper helper = registration.getJeiHelpers().getGuiHelper();
        registration.addRecipeCategories(MAGIC_CRAFT.init(helper));
    }

    @Override
    public void registerVanillaCategoryExtensions(IVanillaCategoryExtensionRegistration registration) {
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        registration.addRecipes(Proxy.getWorld().getRecipeManager().getAllRecipesFor(RecipeRegistrate.RT_RITUAL), MAGIC_CRAFT.getUid());
    }

    @Override
    public void registerRecipeTransferHandlers(IRecipeTransferRegistration registration) {
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(BlockRegistrate.B_RITUAL_CORE.get().asItem().getDefaultInstance(), MAGIC_CRAFT.getUid());
    }

    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration registration) {
    }

    @Override
    public void registerAdvanced(IAdvancedRegistration registration) {
    }

    @Override
    public void onRuntimeAvailable(IJeiRuntime jeiRuntime) {
    }

}
