package dev.hikarishima.lightland.content.common.render;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import dev.hikarishima.lightland.content.common.capability.AbilityPoints;
import dev.hikarishima.lightland.content.common.capability.CapProxy;
import dev.hikarishima.lightland.content.common.capability.LLPlayerData;
import dev.hikarishima.lightland.init.LightLand;
import dev.lcy0x1.base.Proxy;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.client.gui.ForgeIngameGui;
import net.minecraftforge.client.gui.IIngameOverlay;

public class BarOverlay implements IIngameOverlay {

	public static final ResourceLocation GUI_ICONS_LOCATION = new ResourceLocation("textures/gui/icons.png");
	public static final ResourceLocation BARS = new ResourceLocation(LightLand.MODID, "textures/gui/bars.png");

	private boolean shouldRenderSpecial(ForgeIngameGui gui) {
		if (gui.minecraft.player.isRidingJumpable()) return false;
		Player player = Proxy.getClientPlayer();
		if (player == null) return false;
		if (player != gui.minecraft.cameraEntity) return false;
		if (!LLPlayerData.isProper(player)) return false;
		LLPlayerData data = CapProxy.getHandler();
		return data.abilityPoints.profession != null;
	}

	@Override
	public void render(ForgeIngameGui gui, PoseStack mStack, float partialTicks, int width, int height) {
		if (gui.minecraft.options.hideGui) return;
		if (gui.minecraft.player == null) return;
		if (!shouldRenderSpecial(gui)) {
			if (gui.minecraft.player.isRidingJumpable())
				ForgeIngameGui.JUMP_BAR_ELEMENT.render(gui, mStack, partialTicks, width, height);
			else ForgeIngameGui.EXPERIENCE_BAR_ELEMENT.render(gui, mStack, partialTicks, width, height);
			return;
		}
		if (gui.minecraft.player.isRidingJumpable()) {
			ForgeIngameGui.JUMP_BAR_ELEMENT.render(gui, mStack, partialTicks, width, height);
		}
		RenderSystem.setShaderTexture(0, GUI_ICONS_LOCATION);
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
		RenderSystem.disableBlend();

		RenderSystem.setShaderTexture(0, GuiComponent.GUI_ICONS_LOCATION);

		int h = gui.screenHeight - 32 + 3;
		LLPlayerData data = CapProxy.getHandler();

		int len = 81;
		int left = -91;
		if (data.magicAbility.getSpellLoad() > 0) {
			double prog = Math.min(1, data.magicAbility.getMaxSpellEndurance() == 0 ? 0 :
					data.magicAbility.getSpellLoad() * 1.0 / data.magicAbility.getMaxSpellEndurance());
			String str = "Load: " + data.magicAbility.getSpellLoad() + "/" + data.magicAbility.getMaxSpellEndurance();
			RenderSystem.setShaderTexture(0, BARS);
			renderBar(gui, mStack, left, len, h, prog, 20);
			renderText(gui, mStack, str, len, left, h, 0xFF5F32);
		} else {
			renderBar(gui, mStack, left, len, h, gui.minecraft.player.experienceProgress, 64);
			renderText(gui, mStack, "" + gui.minecraft.player.experienceLevel, len, left, h, 8453920);
		}
		left = 10;
		if (data.magicAbility.getMana() < data.magicAbility.getMaxMana()) {
			double prog = data.magicAbility.getMaxMana() == 0 ? 0 :
					data.magicAbility.getMana() * 1.0 / data.magicAbility.getMaxMana();
			String str = "Mana: " + data.magicAbility.getMana() + "/" + data.magicAbility.getMaxMana();
			RenderSystem.setShaderTexture(0, BARS);
			renderBar(gui, mStack, left, len, h, prog, 10);
			renderText(gui, mStack, str, len, left, h, 0x34D1FF);
		} else {
			double prog = data.abilityPoints.exp * 1.0 / AbilityPoints.expRequirement(data.abilityPoints.level);
			String str = "Lv." + data.abilityPoints.level;
			RenderSystem.setShaderTexture(0, BARS);
			renderBar(gui, mStack, left, len, h, prog, 60);
			renderText(gui, mStack, str, len, left, h, 0xFFFFFF);
		}
		RenderSystem.enableBlend();
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
	}

	private void renderBar(ForgeIngameGui gui, PoseStack mStack, int left, int length, int height, double percent, int col) {
		int k = (int) (percent * (length + 1f));
		int l = gui.screenWidth / 2 + left;
		gui.blit(mStack, l, height, 0, col, length / 2, 5);
		gui.blit(mStack, l + length / 2, height, 182 - length + length / 2, col, length - length / 2, 5);
		if (k > 0 && k <= length / 2) {
			gui.blit(mStack, l, height, 0, col + 5, k, 5);
		} else if (k > length / 2) {
			gui.blit(mStack, l, height, 0, col + 5, length / 2, 5);
			gui.blit(mStack, l + length / 2, height, 182 - length + length / 2, col + 5, k - length / 2, 5);
		}
	}

	private void renderText(ForgeIngameGui gui, PoseStack mStack, String s, int len, int shift, int height, int col) {
		int i1 = (gui.screenWidth - gui.getFont().width(s) + len) / 2 + shift;
		int j1 = height - 1;
		gui.getFont().draw(mStack, s, (float) (i1 + 1), (float) j1, 0);
		gui.getFont().draw(mStack, s, (float) (i1 - 1), (float) j1, 0);
		gui.getFont().draw(mStack, s, (float) i1, (float) (j1 + 1), 0);
		gui.getFont().draw(mStack, s, (float) i1, (float) (j1 - 1), 0);
		gui.getFont().draw(mStack, s, (float) i1, (float) j1, col);
	}

}
