package dev.hikarishima.lightland.content.common.render;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import dev.hikarishima.lightland.init.LightLand;
import net.minecraft.Util;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.client.gui.ForgeIngameGui;
import net.minecraftforge.client.gui.IIngameOverlay;

public class HealthOverlay implements IIngameOverlay {

    public static final ResourceLocation GUI_ICONS_LOCATION = new ResourceLocation("textures/gui/icons.png");
    public static final ResourceLocation BAR_GUI_ICONS = new ResourceLocation(LightLand.MODID, "textures/gui/icons.png");

    @Override
    public void render(ForgeIngameGui gui, PoseStack mStack, float partialTicks, int width, int height) {
        if (gui.minecraft.options.hideGui) return;
        if (!gui.shouldDrawSurvivalElements()) return;
        Player player = (Player) gui.minecraft.getCameraEntity();
        if (player == null) return;
        gui.setupOverlayRenderState(true, false);
        RenderSystem.setShaderTexture(0, GUI_ICONS_LOCATION);
        RenderSystem.enableBlend();

        AttributeInstance attrMaxHealth = player.getAttribute(Attributes.MAX_HEALTH);
        int healthMax = (int) Math.ceil(Math.max((float) attrMaxHealth.getValue(), player.getHealth()));
        int absorb = Mth.ceil(player.getAbsorptionAmount());
        int healthRows = Mth.ceil((healthMax + absorb) / 20.0F);
        if (healthRows <= 0) {
            renderIcons(gui, mStack, player, width, height);
        } else {
            renderBars(gui, mStack, player, width, height);
        }
        RenderSystem.disableBlend();
    }

    private static final int WITHER = 4;
    private static final int POISON = 5;
    private static final int FROZEN = 6;
    private static final int HEALTH = 3;
    private static final int ARMOR = 1;
    private static final int TOUGH = 2;


    private static void renderBars(ForgeIngameGui gui, PoseStack mStack, Player player, int width, int height) {
        double max_health = player.getAttribute(Attributes.MAX_HEALTH).getValue();
        double health = player.getHealth();
        double absorb = player.getAbsorptionAmount();
        boolean poison = player.hasEffect(MobEffects.POISON);
        boolean wither = player.hasEffect(MobEffects.WITHER);
        boolean frozen = player.isFullyFrozen();
        double armor = player.getArmorValue();
        double tough = player.getAttribute(Attributes.ARMOR_TOUGHNESS).getValue();

        int perc_health = (int) (79 * health / max_health) + 1;
        int perc_absorv = (int) (79 * Math.min(1, absorb / max_health) + 1);
        int perc_armor = armor == 0 ? 0 : (int) (80 * Math.min(1, armor / max_health) + 1);
        int perc_tough = tough == 0 ? 0 : (int) (80 * Math.min(Math.min(1, armor / max_health), tough / max_health) + 1);

        int health_color = wither ? WITHER : poison ? POISON : frozen ? FROZEN : HEALTH;

        int left = width / 2 - 91;
        int top = height - gui.left_height;

        RenderSystem.setShaderTexture(0, BAR_GUI_ICONS);
        gui.blit(mStack, left, top, 0, 0, 81, 8);
        gui.blit(mStack, left, top, 0, health_color * 8, perc_health, 8);
        gui.blit(mStack, left, top, 0, 7 * 8, perc_absorv, 8);
        gui.blit(mStack, left, top, 0, 8, perc_armor, 8);
        gui.blit(mStack, left, top, 0, 16, perc_tough, 8);

    }

    private static void renderIcons(ForgeIngameGui gui, PoseStack mStack, Player player, int width, int height) {
        int health = Mth.ceil(player.getHealth());
        boolean blink = gui.healthBlinkTime > (long) gui.tickCount && (gui.healthBlinkTime - (long) gui.tickCount) / 3L % 2L == 1L;

        if (health < gui.lastHealth && player.invulnerableTime > 0) {
            gui.lastHealthTime = Util.getMillis();
            gui.healthBlinkTime = gui.tickCount + 20;
        } else if (health > gui.lastHealth && player.invulnerableTime > 0) {
            gui.lastHealthTime = Util.getMillis();
            gui.healthBlinkTime = gui.tickCount + 10;
        }

        if (Util.getMillis() - gui.lastHealthTime > 1000L) {
            gui.lastHealth = health;
            gui.displayHealth = health;
            gui.lastHealthTime = Util.getMillis();
        }

        gui.lastHealth = health;
        int healthLast = gui.displayHealth;

        AttributeInstance attrMaxHealth = player.getAttribute(Attributes.MAX_HEALTH);
        float healthMax = Math.max((float) attrMaxHealth.getValue(), Math.max(healthLast, health));
        int absorb = Mth.ceil(player.getAbsorptionAmount());

        int healthRows = Mth.ceil((healthMax + absorb) / 2.0F / 10.0F);
        int rowHeight = Math.max(10 - (healthRows - 2), 3);

        gui.random.setSeed(gui.tickCount * 312871L);

        int left = width / 2 - 91;
        int top = height - gui.left_height;
        gui.left_height += (healthRows * rowHeight);
        if (rowHeight != 10) gui.left_height += 10 - rowHeight;

        int regen = -1;
        if (player.hasEffect(MobEffects.REGENERATION)) {
            regen = gui.tickCount % Mth.ceil(healthMax + 5.0F);
        }

        renderHearts(gui, mStack, player, left, top, rowHeight, regen, healthMax, health, healthLast, absorb, blink);

        left = width / 2 - 91;
        top = height - gui.left_height;

        int level = gui.minecraft.player.getArmorValue();
        for (int i = 1; level > 0 && i < 20; i += 2) {
            if (i < level) {
                gui.blit(mStack, left, top, 34, 9, 9, 9);
            } else if (i == level) {
                gui.blit(mStack, left, top, 25, 9, 9, 9);
            } else {
                gui.blit(mStack, left, top, 16, 9, 9, 9);
            }
            left += 8;
        }
        gui.left_height += 10;
    }


    protected static void renderHearts(ForgeIngameGui gui, PoseStack stack, Player player, int left, int top, int rowHeight,
                                       int regen, float max, int health, int healthLast, int absorb, boolean blink) {
        HeartType gui$hearttype = HeartType.forPlayer(player);
        int i = 9 * (player.level.getLevelData().isHardcore() ? 5 : 0);
        int j = Mth.ceil((double) max / 2.0D);
        int k = Mth.ceil((double) absorb / 2.0D);
        int l = j * 2;

        for (int i1 = j + k - 1; i1 >= 0; --i1) {
            int j1 = i1 / 10;
            int k1 = i1 % 10;
            int l1 = left + k1 * 8;
            int i2 = top - j1 * rowHeight;
            if (health + absorb <= 4) {
                i2 += gui.random.nextInt(2);
            }

            if (i1 < j && i1 == regen) {
                i2 -= 2;
            }

            renderHeart(gui, stack, HeartType.CONTAINER, l1, i2, i, blink, false);
            int j2 = i1 * 2;
            boolean flag = i1 >= j;
            if (flag) {
                int k2 = j2 - l;
                if (k2 < absorb) {
                    boolean flag1 = k2 + 1 == absorb;
                    renderHeart(gui, stack, gui$hearttype == HeartType.WITHERED ? gui$hearttype : HeartType.ABSORBING, l1, i2, i, false, flag1);
                }
            }

            if (blink && j2 < healthLast) {
                boolean flag2 = j2 + 1 == healthLast;
                renderHeart(gui, stack, gui$hearttype, l1, i2, i, true, flag2);
            }

            if (j2 < health) {
                boolean flag3 = j2 + 1 == health;
                renderHeart(gui, stack, gui$hearttype, l1, i2, i, false, flag3);
            }
        }

    }

    private static void renderHeart(ForgeIngameGui gui, PoseStack stack, HeartType type, int x, int y, int ind, boolean blink, boolean half) {
        gui.blit(stack, x, y, type.getX(half, blink), ind, 9, 9);
    }

    enum HeartType {
        CONTAINER(0, false),
        NORMAL(2, true),
        POISIONED(4, true),
        WITHERED(6, true),
        ABSORBING(8, false),
        FROZEN(9, false);

        private final int index;
        private final boolean canBlink;

        HeartType(int ind, boolean blink) {
            this.index = ind;
            this.canBlink = blink;
        }

        public int getX(boolean half, boolean blink) {
            int i;
            if (this == CONTAINER) {
                i = blink ? 1 : 0;
            } else {
                int j = half ? 1 : 0;
                int k = this.canBlink && blink ? 2 : 0;
                i = j + k;
            }

            return 16 + (this.index * 2 + i) * 9;
        }

        static HeartType forPlayer(Player p_168733_) {
            HeartType type;
            if (p_168733_.hasEffect(MobEffects.POISON)) {
                type = POISIONED;
            } else if (p_168733_.hasEffect(MobEffects.WITHER)) {
                type = WITHERED;
            } else if (p_168733_.isFullyFrozen()) {
                type = FROZEN;
            } else {
                type = NORMAL;
            }
            return type;
        }
    }


}
