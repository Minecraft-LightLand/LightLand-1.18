package dev.hikarishima.lightland.content.common.render;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import dev.hikarishima.lightland.content.common.capability.CapProxy;
import dev.hikarishima.lightland.content.common.capability.LLPlayerData;
import dev.hikarishima.lightland.content.common.capability.SkillCap;
import dev.hikarishima.lightland.content.skill.Skill;
import dev.hikarishima.lightland.content.skill.SkillConfig;
import dev.hikarishima.lightland.content.skill.SkillData;
import dev.hikarishima.lightland.init.LightLand;
import dev.lcy0x1.base.Proxy;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.client.gui.ForgeIngameGui;
import net.minecraftforge.client.gui.IIngameOverlay;

public class SkillOverlay implements IIngameOverlay {

    protected static final ResourceLocation SPELL_BAR = new ResourceLocation(LightLand.MODID, "textures/gui/widgets.png");

    @Override
    public void render(ForgeIngameGui gui, PoseStack mStack, float partialTicks, int width, int height) {
        Player player = Proxy.getClientPlayer();
        if (player == null) return;
        if (player != gui.minecraft.cameraEntity) return;
        if (!LLPlayerData.isProper(player)) return;
        LLPlayerData data = CapProxy.getHandler();
        int x = width / 2 + 91 + 8;
        int y = height - 16 - 6;
        RenderSystem.setShaderTexture(0, SPELL_BAR);
        gui.blit(mStack, x, y, 0, 0, 41, 22);
        gui.blit(mStack, x + 41, y, 141, 0, 41, 22);
        for (int i = 0; i < 4; i++) {
            if (data.skillCap.list.size() > i) {
                renderSkill(gui, mStack, data.skillCap.list.get(i), x + i * 20 + 3, y + 3);
            }
        }
    }

    private static <S extends Skill<C, D>, C extends SkillConfig<D>, D extends SkillData> void
    renderSkill(ForgeIngameGui gui, PoseStack stack, SkillCap.Cont<S, C, D> cont, int x, int y) {
        S skill = cont.skill;
        D skillData = cont.data;
        C config = skill.getConfig();
        if (config == null) return;
        ResourceLocation rl = skill.getRegistryName();
        RenderSystem.setShaderTexture(0, new ResourceLocation(rl.getNamespace(), "textures/skill/" + rl.getPath()));
        gui.blit(stack, x, y, 0, 0, 16, 16);
        float cooldown = skillData.cooldown * 1.0f / config.getCooldown(skillData);
        if (cooldown > 0) {
            renderCooldown(x, y, cooldown);
        }
    }

    private static void renderCooldown(int x, int y, float f) {
        RenderSystem.disableDepthTest();
        RenderSystem.disableTexture();
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        Tesselator tesselator1 = Tesselator.getInstance();
        BufferBuilder bufferbuilder1 = tesselator1.getBuilder();
        fillRect(bufferbuilder1, x, y + Mth.floor(16.0F * (1.0F - f)), 16, Mth.ceil(16.0F * f), 255, 255, 255, 127);
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

}
