package dev.hikarishima.lightland.content.common.render;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import dev.hikarishima.lightland.content.common.capability.CapProxy;
import dev.hikarishima.lightland.content.common.capability.LLPlayerData;
import dev.hikarishima.lightland.init.LightLand;
import dev.lcy0x1.base.Proxy;
import net.minecraft.client.AttackIndicatorStatus;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameType;
import net.minecraftforge.client.gui.ForgeIngameGui;
import net.minecraftforge.client.gui.IIngameOverlay;

public class SpellOverlay implements IIngameOverlay {

	protected static final ResourceLocation SPELL_BAR = new ResourceLocation(LightLand.MODID, "textures/gui/widgets.png");
	protected static final ResourceLocation WIDGETS_LOCATION = new ResourceLocation("textures/gui/widgets.png");

	private boolean shouldRenderSpecial(ForgeIngameGui gui) {
		Player player = Proxy.getClientPlayer();
		if (player == null)
			return false;
		if (gui.minecraft.getCameraEntity() != player)
			return false;
		if (gui.minecraft.options.hideGui)
			return false;
		if (gui.minecraft.gameMode != null && gui.minecraft.gameMode.getPlayerMode() == GameType.SPECTATOR)
			return false;
		if (!LLPlayerData.isProper(player))
			return false;
		LLPlayerData data = CapProxy.getHandler();
		return data.magicAbility.spell_level > 0;
	}

	@Override
	public void render(ForgeIngameGui gui, PoseStack mStack, float partialTicks, int width, int height) {
		if (!shouldRenderSpecial(gui)) {
			ForgeIngameGui.HOTBAR_ELEMENT.render(gui, mStack, partialTicks, width, height);
			return;
		}
		gui.setupOverlayRenderState(true, false);
		Player player = Proxy.getClientPlayer();
		int x = CapProxy.getHandler().magicAbility.spell_level;
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		ItemStack itemstack = player.getOffhandItem();
		HumanoidArm humanoidarm = player.getMainArm().getOpposite();
		int i = gui.screenWidth / 2;
		int j = gui.getBlitOffset();
		gui.setBlitOffset(-90);
		int sel = player.getInventory().selected;

		// render hotbar
		if (x > 0) {
			RenderSystem.setShaderTexture(0, SPELL_BAR);
			int tlen = x == 9 ? 182 : 1 + x * 20;
			gui.blit(mStack, i - 91, gui.screenHeight - 22, 0, 0, tlen, 22);
		}
		if (x < 9) {
			RenderSystem.setShaderTexture(0, WIDGETS_LOCATION);
			int left = x == 0 ? i - 91 : i - 91 + 20 * x + 1;
			int tleft = x == 0 ? 0 : 20 * x + 1;
			int tlen = x == 0 ? 182 : 181 - 20 * x;
			gui.blit(mStack, left, gui.screenHeight - 22, tleft, 0, tlen, 22);
		}
		if (sel < x) {
			RenderSystem.setShaderTexture(0, SPELL_BAR);
			gui.blit(mStack, i - 91 - 1 + sel * 20, gui.screenHeight - 22 - 1, 0, 22, 24, 22);
		}
		if (sel >= x) {
			RenderSystem.setShaderTexture(0, WIDGETS_LOCATION);
			gui.blit(mStack, i - 91 - 1 + sel * 20, gui.screenHeight - 22 - 1, 0, 22, 24, 22);
		}

		// render offhand bar
		RenderSystem.setShaderTexture(0, WIDGETS_LOCATION);
		if (!itemstack.isEmpty()) {
			if (humanoidarm == HumanoidArm.LEFT) {
				gui.blit(mStack, i - 91 - 29, gui.screenHeight - 23, 24, 22, 29, 24);
			} else {
				gui.blit(mStack, i + 91, gui.screenHeight - 23, 53, 22, 29, 24);
			}
		}

		// render items
		gui.setBlitOffset(j);
		RenderSystem.enableBlend();
		RenderSystem.defaultBlendFunc();
		int i1 = 1;

		for (int j1 = 0; j1 < 9; ++j1) {
			int k1 = i - 90 + j1 * 20 + 2;
			int l1 = gui.screenHeight - 16 - 3;
			gui.renderSlot(k1, l1, partialTicks, player, player.getInventory().items.get(j1), i1++);
		}

		if (!itemstack.isEmpty()) {
			int j2 = gui.screenHeight - 16 - 3;
			if (humanoidarm == HumanoidArm.LEFT) {
				gui.renderSlot(i - 91 - 26, j2, partialTicks, player, itemstack, i1++);
			} else {
				gui.renderSlot(i + 91 + 10, j2, partialTicks, player, itemstack, i1++);
			}
		}

		// render attack indicator
		if (gui.minecraft.options.attackIndicator == AttackIndicatorStatus.HOTBAR) {
			float f = gui.minecraft.player.getAttackStrengthScale(0.0F);
			if (f < 1.0F) {
				int k2 = gui.screenHeight - 20;
				int l2 = i + 91 + 6;
				if (humanoidarm == HumanoidArm.RIGHT) {
					l2 = i - 91 - 22;
				}

				RenderSystem.setShaderTexture(0, GuiComponent.GUI_ICONS_LOCATION);
				int i2 = (int) (f * 19.0F);
				RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
				gui.blit(mStack, l2, k2, 0, 94, 18, 18);
				gui.blit(mStack, l2, k2 + 18 - i2, 18, 112 - i2, 18, i2);
			}
		}

		RenderSystem.disableBlend();

	}

}
