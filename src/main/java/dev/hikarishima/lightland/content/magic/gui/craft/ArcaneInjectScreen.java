package dev.hikarishima.lightland.content.magic.gui.craft;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.hikarishima.lightland.compat.jei.screen.ExtraInfo;
import dev.hikarishima.lightland.content.common.capability.player.CapProxy;
import dev.hikarishima.lightland.content.magic.gui.AbstractHexGui;
import dev.hikarishima.lightland.content.magic.products.MagicElement;
import dev.hikarishima.lightland.init.data.LangData;
import dev.xkmc.l2library.menu.BaseContainerScreen;
import dev.xkmc.l2library.menu.SpriteManager;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.player.Inventory;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Map;

@ParametersAreNonnullByDefault
public class ArcaneInjectScreen extends BaseContainerScreen<ArcaneInjectContainer> implements ExtraInfo<Map.Entry<MagicElement, Integer>> {

	public ArcaneInjectScreen(ArcaneInjectContainer cont, Inventory plInv, Component title) {
		super(cont, plInv, title);
	}

	@Override
	protected void renderBg(PoseStack matrix, float partial, int mx, int my) {
		mx -= getGuiLeft();
		my -= getGuiTop();
		SpriteManager sm = menu.sprite;
		SpriteManager.ScreenRenderer sr = sm.getRenderer(this);
		sr.start(matrix);
		if (menu.err == ArcaneInjectContainer.Error.PASS)
			sr.draw(matrix, "arrow", sm.within("arrow", mx, my) ? "arrow_2" : "arrow_1");
		else if (menu.err != ArcaneInjectContainer.Error.NO_ITEM)
			sr.draw(matrix, "arrow", "arrow_3");
		getInfo((ex, ey, w, h, ent) -> {
			int count = ent.getValue();
			int have = CapProxy.getHandler().magicHolder.getElement(ent.getKey());
			AbstractHexGui.drawElement(matrix, ex + getGuiLeft() + 9, ey + getGuiTop() + 9, ent.getKey(), "" + count, have >= count ? 0xFFFFFF : 0xFF0000);
		});
	}

	@Override
	protected void renderTooltip(PoseStack matrix, int mx, int my) {
		super.renderTooltip(matrix, mx, my);
		if (menu.sprite.within("arrow", mx - getGuiLeft(), my - getGuiTop()) &&
				menu.err != ArcaneInjectContainer.Error.NO_ITEM)
			renderTooltip(matrix, menu.err.getDesc(menu), mx, my);
		getInfoMouse(mx - getGuiLeft(), my - getGuiTop(), (ex, ey, w, h, ent) -> {
			int count = ent.getValue();
			int have = CapProxy.getHandler().magicHolder.getElement(ent.getKey());
			TranslatableComponent text = LangData.IDS.GUI_SPELL_CRAFT_ELEM_COST.get(count, have);
			if (have < count) text.withStyle(ChatFormatting.RED);
			renderTooltip(matrix, text, mx, my);
		});
	}

	@Override
	public boolean mouseClicked(double mx, double my, int button) {
		SpriteManager sm = menu.sprite;
		if (menu.err == ArcaneInjectContainer.Error.PASS && sm.within("arrow", mx - getGuiLeft(), my - getGuiTop())) {
			click(0);
			return true;
		}
		return super.mouseClicked(mx, my, button);
	}

	@Override
	public void getInfo(Con<Map.Entry<MagicElement, Integer>> con) {
		int x = menu.sprite.getComp("output_slot").x + 18 - 1;
		int y = menu.sprite.getComp("output_slot").y - 1;
		int i = 0;
		for (Map.Entry<MagicElement, Integer> ent : menu.map.entrySet()) {
			int ex = x + i % 3 * 18;
			int ey = y + i / 3 * 18;
			con.apply(ex, ey, 18, 18, ent);
			i++;
		}
	}

}
