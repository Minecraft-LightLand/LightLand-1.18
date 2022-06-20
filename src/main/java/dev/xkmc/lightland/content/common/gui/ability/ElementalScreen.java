package dev.xkmc.lightland.content.common.gui.ability;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.xkmc.l2library.repack.registrate.util.entry.RegistryEntry;
import dev.xkmc.lightland.content.common.capability.player.CapProxy;
import dev.xkmc.lightland.content.common.capability.player.LLPlayerData;
import dev.xkmc.lightland.content.magic.gui.AbstractHexGui;
import dev.xkmc.lightland.content.magic.products.MagicElement;
import dev.xkmc.lightland.init.data.LangData;
import dev.xkmc.lightland.init.special.LightLandRegistry;
import dev.xkmc.lightland.init.special.MagicRegistry;
import dev.xkmc.lightland.network.packets.CapToServer;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.locale.Language;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.List;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class ElementalScreen extends AbstractAbilityScreen {

	public static final Component TITLE = LangData.IDS.GUI_ELEMENT.get();
	private static final int RADIUS = 30;

	public static boolean canAccess() {
		LLPlayerData handler = CapProxy.getHandler();
		return handler.abilityPoints.canLevelElement() ||
				LightLandRegistry.ELEMENT.get().getValues().stream()
						.anyMatch(e -> handler.magicHolder.getElementalMastery(e) > 0);
	}

	protected ElementalScreen() {
		super(AbilityTab.ELEMENT, TITLE);
	}

	@Override
	protected void renderInside(PoseStack matrix, int w, int h, int mx, int my, float partial) {
		fill(matrix, 0, 0, w, h, 0xFF606060);
		matrix.pushPose();
		matrix.translate(w / 2f, h / 2f, 0);
		mx -= w / 2;
		my -= h / 2;
		LLPlayerData handler = CapProxy.getHandler();
		for (ElemType e : ElemType.values()) {
			e.renderElem(handler, matrix, mx, my);
		}
		matrix.popPose();
	}

	@Override
	public boolean innerMouseClick(int w, int h, double mx, double my) {
		LLPlayerData handler = CapProxy.getHandler();
		if (!handler.abilityPoints.canLevelElement())
			return false;
		for (ElemType e : ElemType.values()) {
			if (e.within(mx - w / 2f, my - h / 2f)) {
				if (handler.magicHolder.addElementalMastery(e.elem.get())) {
					CapToServer.addElemMastery(e.elem.get());
					handler.abilityPoints.levelElement();
				}
			}
		}
		return false;
	}

	@Override
	public void renderInnerTooltip(PoseStack matrix, int w, int h, int mx, int my) {
		LLPlayerData handler = CapProxy.getHandler();
		for (ElemType e : ElemType.values()) {
			if (e.within(mx - w / 2f, my - h / 2f)) {
				int lv = handler.magicHolder.getElementalMastery(e.elem.get());
				int count = handler.magicHolder.getElement(e.elem.get());
				int rem = handler.abilityPoints.element;
				List<FormattedText> list = new ArrayList<>();
				list.add(e.elem.get().getDesc());
				list.add(LangData.IDS.GUI_ELEMENT_LV.get(lv));
				list.add(LangData.IDS.GUI_ELEMENT_COUNT.get(count));
				list.add(LangData.IDS.GUI_ELEMENT_COST.get(1, rem));
				renderTooltip(matrix, Language.getInstance().getVisualOrder(list), mx, my);
			}
		}
	}

	public enum ElemType {
		E(0, RADIUS, MagicRegistry.ELEM_EARTH),
		W(-RADIUS, 0, MagicRegistry.ELEM_WATER),
		A(0, -RADIUS, MagicRegistry.ELEM_AIR),
		F(RADIUS, 0, MagicRegistry.ELEM_FIRE),
		Q(0, 0, MagicRegistry.ELEM_QUINT);

		public final int x, y;
		public final RegistryEntry<MagicElement> elem;

		ElemType(int x, int y, RegistryEntry<MagicElement> elem) {
			this.x = x;
			this.y = y;
			this.elem = elem;
		}

		public void renderElem(LLPlayerData handler, PoseStack matrix, int mx, int my) {
			int lv = handler.magicHolder.getElementalMastery(elem.get());
			int count = handler.magicHolder.getElement(elem.get());
			AbstractHexGui.drawElement(matrix, x, y, elem.get(), "" + lv);
			if (within(mx, my) && handler.abilityPoints.canLevelElement())
				fill(matrix, x - 8, y - 8, x + 8, y + 8, 0x80FFFFFF);
		}

		public boolean within(double mx, double my) {
			return mx > x - 8 && mx < x + 8 && my > y - 8 && my < y + 8;
		}

	}

}
