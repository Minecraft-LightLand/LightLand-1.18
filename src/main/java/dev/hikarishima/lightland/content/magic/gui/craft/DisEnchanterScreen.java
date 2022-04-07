package dev.hikarishima.lightland.content.magic.gui.craft;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.hikarishima.lightland.compat.jei.screen.ExtraInfo;
import dev.hikarishima.lightland.content.magic.gui.AbstractHexGui;
import dev.hikarishima.lightland.content.magic.products.MagicElement;
import dev.lcy0x1.menu.BaseContainerScreen;
import dev.lcy0x1.menu.SpriteManager;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Map;

@ParametersAreNonnullByDefault
public class DisEnchanterScreen extends BaseContainerScreen<DisEnchanterContainer> implements ExtraInfo<Map.Entry<MagicElement, Integer>> {

	public DisEnchanterScreen(DisEnchanterContainer cont, Inventory plInv, Component title) {
		super(cont, plInv, title);
	}

	@Override
	protected void renderBg(PoseStack matrix, float partial, int mx, int my) {
		mx -= getGuiLeft();
		my -= getGuiTop();
		SpriteManager sm = menu.sprite;
		SpriteManager.ScreenRenderer sr = sm.getRenderer(this);
		sr.start(matrix);
		if (!menu.map.isEmpty()) {
			sr.draw(matrix, "arrow", sm.within("arrow", mx, my) ? "arrow_2" : "arrow_1");
			getInfo((x, y, w, h, ent) -> {
				AbstractHexGui.drawElement(matrix, x, y, ent.getKey(), "" + ent.getValue());
			});
		}
	}

	@Override
	public boolean mouseClicked(double mx, double my, int button) {
		SpriteManager sm = menu.sprite;
		if (!menu.container.isEmpty() && sm.within("arrow", mx - getGuiLeft(), my - getGuiTop())) {
			click(0);
			return true;
		}
		return super.mouseClicked(mx, my, button);
	}

	@Override
	public void getInfo(Con<Map.Entry<MagicElement, Integer>> con) {
		if (!menu.map.isEmpty()) {
			int x = 8 + 8 + 18 * 2 + getGuiLeft();
			int y = menu.sprite.getComp("main_slot").y + getGuiTop() + 8;
			for (Map.Entry<MagicElement, Integer> ent : menu.map.entrySet()) {
				con.apply(x += 18, y, 16, 16, ent);
			}
		}
	}
}
