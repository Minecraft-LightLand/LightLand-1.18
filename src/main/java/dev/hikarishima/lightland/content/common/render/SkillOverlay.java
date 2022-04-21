package dev.hikarishima.lightland.content.common.render;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import dev.hikarishima.lightland.content.common.capability.player.CapProxy;
import dev.hikarishima.lightland.content.common.capability.player.LLPlayerData;
import dev.hikarishima.lightland.content.common.capability.player.SkillCap;
import dev.hikarishima.lightland.content.skill.internal.Skill;
import dev.hikarishima.lightland.content.skill.internal.SkillConfig;
import dev.hikarishima.lightland.content.skill.internal.SkillData;
import dev.hikarishima.lightland.init.data.LangData;
import dev.hikarishima.lightland.init.special.SkillRegistry;
import dev.lcy0x1.menu.OverlayManager;
import dev.lcy0x1.util.Proxy;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.client.gui.ForgeIngameGui;

public class SkillOverlay {

	public static void render(OverlayManager.ScreenRenderer renderer) {
		Player player = Proxy.getClientPlayer();
		if (player == null) return;
		if (player != renderer.cameraEntity) return;
		if (!LLPlayerData.isProper(player)) return;
		LLPlayerData data = CapProxy.getHandler();
		renderer.draw("skill_list");
		OverlayManager.Rect rect = renderer.get("skill_list");
		int x = renderer.gui.screenWidth / 2 + rect.sx + 6;
		int y = renderer.gui.screenHeight + rect.sy + 3;
		for (int i = 0; i < SkillRegistry.MAX; i++) {
			if (data.skillCap.list.size() > i) {
				renderSkill(renderer.gui, renderer.stack, data.skillCap.list.get(i), x + i * 20 + 3, y + 3, i);
			}
		}
		renderer.start();
	}

	private static <S extends Skill<C, D>, C extends SkillConfig<D>, D extends SkillData> void
	renderSkill(ForgeIngameGui gui, PoseStack stack, SkillCap.Cont<S, C, D> cont, int x, int y, int i) {
		S skill = cont.skill;
		D skillData = cont.data;
		C config = skill.getConfig();
		if (config == null) return;
		RenderSystem.setShaderTexture(0, skill.getIcon());
		GuiComponent.blit(stack, x, y, 0, 0, 16, 16, 16, 16);
		float cooldown = skillData.cooldown * 1.0f / config.getCooldown(skillData);
		if (cooldown > 0) {
			renderCooldown(x, y, cooldown);
			int sec = (int) Math.ceil(skillData.cooldown / 20f);
			if (sec <= 99) renderText(gui, stack, "" + sec, x + 8, y + 8);
		}
		renderText(gui, stack, LangData.Keys.values()[i].map.getKey().getDisplayName().getContents(), x + 8, y - 8);
	}

	private static void renderCooldown(int x, int y, float f) {
		RenderSystem.disableDepthTest();
		RenderSystem.disableTexture();
		RenderSystem.enableBlend();
		RenderSystem.defaultBlendFunc();
		Tesselator tex = Tesselator.getInstance();
		BufferBuilder builder = tex.getBuilder();
		fillRect(builder, x, y, 16, 16, 0, 0, 0, 127);
		fillRect(builder, x, y + Mth.floor(16.0F * (1.0F - f)), 16, Mth.ceil(16.0F * f), 255, 255, 255, 127);
		RenderSystem.enableTexture();
		RenderSystem.enableDepthTest();
	}

	private static void fillRect(BufferBuilder builder, int x, int y, int w, int h, int r, int g, int b, int a) {
		RenderSystem.setShader(GameRenderer::getPositionColorShader);
		builder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
		builder.vertex(x, y, 0.0D).color(r, g, b, a).endVertex();
		builder.vertex(x, y + h, 0.0D).color(r, g, b, a).endVertex();
		builder.vertex(x + w, y + h, 0.0D).color(r, g, b, a).endVertex();
		builder.vertex(x + w, y, 0.0D).color(r, g, b, a).endVertex();
		builder.end();
		BufferUploader.end(builder);
	}

	public static void renderText(ForgeIngameGui gui, PoseStack mStack, String s, int cx, int cy) {
		int i1 = cx - gui.getFont().width(s) / 2;
		int j1 = cy - 3;
		gui.getFont().draw(mStack, s, (float) (i1 + 1), (float) j1, 0);
		gui.getFont().draw(mStack, s, (float) (i1 - 1), (float) j1, 0);
		gui.getFont().draw(mStack, s, (float) i1, (float) (j1 + 1), 0);
		gui.getFont().draw(mStack, s, (float) i1, (float) (j1 - 1), 0);
		gui.getFont().draw(mStack, s, (float) i1, (float) j1, 0xFFFFFF);
	}

}
