package dev.hikarishima.lightland.compat.jei.ingredients;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.hikarishima.lightland.content.common.capability.player.CapProxy;
import dev.hikarishima.lightland.content.magic.gui.AbstractHexGui;
import dev.hikarishima.lightland.init.data.LangData;
import mezz.jei.api.MethodsReturnNonnullByDefault;
import mezz.jei.api.ingredients.IIngredientRenderer;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.item.TooltipFlag;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.List;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class ElemIngredientRenderer implements IIngredientRenderer<ElementIngredient> {

	@Override
	public void render(PoseStack matrixStack, int x, int y, @Nullable ElementIngredient elem) {
		if (elem != null)
			AbstractHexGui.drawElement(matrixStack, x + 8, y + 8, elem.elem, elem.count > 1 ? "" + elem.count : "");
	}

	@Override
	public List<Component> getTooltip(ElementIngredient elem, TooltipFlag iTooltipFlag) {
		int has = CapProxy.getHandler().magicHolder.getElement(elem.elem);
		List<Component> list = new ArrayList<>();
		list.add(elem.elem.getDesc());
		TranslatableComponent txt = LangData.IDS.GUI_ELEMENT_COUNT.get(has);
		if (has < elem.count)
			txt.withStyle(ChatFormatting.RED);
		list.add(txt);
		return list;
	}
}
