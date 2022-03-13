package dev.hikarishima.lightland.compat.jei.ingredients;

import dev.hikarishima.lightland.compat.jei.LightLandJeiPlugin;
import mezz.jei.api.MethodsReturnNonnullByDefault;
import mezz.jei.api.ingredients.IIngredientHelper;
import mezz.jei.api.ingredients.IIngredientType;
import mezz.jei.api.ingredients.subtypes.UidContext;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class ElemIngredientHelper implements IIngredientHelper<ElementIngredient> {

	@Nullable
	@Override
	public ElementIngredient getMatch(Iterable<ElementIngredient> iterable, ElementIngredient magicElement, UidContext context) {
		for (ElementIngredient elem : iterable)
			if (elem.elem == magicElement.elem)
				return elem;
		return null;
	}

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
		return magicElement.elem.getIcon().toString();
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
