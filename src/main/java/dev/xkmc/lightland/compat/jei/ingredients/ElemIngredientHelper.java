package dev.xkmc.lightland.compat.jei.ingredients;

import dev.xkmc.lightland.compat.jei.LightLandJeiPlugin;
import mezz.jei.api.ingredients.IIngredientHelper;
import mezz.jei.api.ingredients.IIngredientType;
import mezz.jei.api.ingredients.subtypes.UidContext;
import net.minecraft.MethodsReturnNonnullByDefault;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class ElemIngredientHelper implements IIngredientHelper<ElementIngredient> {

	@Override
	public IIngredientType<ElementIngredient> getIngredientType() {
		return LightLandJeiPlugin.INSTANCE.ELEM_TYPE;
	}

	@Override
	public String getDisplayName(ElementIngredient magicElement) {
		return magicElement.elem.getDesc().getContents();
	}

	@Override
	public String getUniqueId(ElementIngredient magicElement, UidContext context) {
		return magicElement.elem.getID();
	}

	@Override
	public String getModId(ElementIngredient magicElement) {
		return magicElement.elem.getRegistryName().getNamespace();
	}

	@Override
	public String getResourceId(ElementIngredient magicElement) {
		return magicElement.elem.getRegistryName().getPath();
	}

	@Override
	public ElementIngredient copyIngredient(ElementIngredient magicElement) {
		return new ElementIngredient(magicElement);
	}

	@Override
	public String getErrorInfo(@Nullable ElementIngredient magicElement) {
		return "magic element error";
	}
}
