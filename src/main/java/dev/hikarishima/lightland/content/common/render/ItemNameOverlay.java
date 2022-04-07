package dev.hikarishima.lightland.content.common.render;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraftforge.client.gui.ForgeIngameGui;
import net.minecraftforge.client.gui.IIngameOverlay;

public class ItemNameOverlay implements IIngameOverlay {
	@Override
	public void render(ForgeIngameGui gui, PoseStack mStack, float partialTicks, int width, int height) {
		mStack.pushPose();
		mStack.translate(0, -30, 0);
		ForgeIngameGui.ITEM_NAME_ELEMENT.render(gui, mStack, partialTicks, width, height);
		mStack.popPose();
	}
}
