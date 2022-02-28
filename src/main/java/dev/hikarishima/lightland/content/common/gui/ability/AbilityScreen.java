package dev.hikarishima.lightland.content.common.gui.ability;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import dev.hikarishima.lightland.content.common.capability.AbilityPoints;
import dev.hikarishima.lightland.content.common.capability.CapProxy;
import dev.hikarishima.lightland.content.common.capability.LLPlayerData;
import dev.hikarishima.lightland.content.magic.gui.AbstractHexGui;
import dev.hikarishima.lightland.init.LightLand;
import dev.hikarishima.lightland.init.data.LangData;
import dev.hikarishima.lightland.network.packets.CapToServer;
import net.minecraft.ChatFormatting;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.locale.Language;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class AbilityScreen extends AbstractAbilityScreen {

    public static final Component TITLE = LangData.IDS.GUI_ABILITY.get();
    private static final int H_DIS = 50, Y_DIS = 30;

    public AbilityScreen() {
        super(AbilityTab.ABILITY, TITLE);
    }

    @Override
    protected void renderInside(PoseStack matrix, int w, int h, int mx, int my, float partial) {
        fill(matrix, 0, 0, w, h, 0xFF606060);
        matrix.pushPose();
        matrix.translate(w / 2f, h / 2f, 0);
        mx -= w / 2;
        my -= h / 2;
        LLPlayerData handler = CapProxy.getHandler();
        for (AbilityType e : AbilityType.values()) {
            e.render(handler, matrix, mx, my);
        }
        matrix.popPose();

    }

    @Override
    public boolean innerMouseClick(int w, int h, double mx, double my) {
        LLPlayerData handler = CapProxy.getHandler();
        for (AbilityType e : AbilityType.values()) {
            if (e.within(mx - w / 2f, my - h / 2f)) {
                if (e.type.checkLevelUp(handler) == null) {
                    e.type.doLevelUp(handler);
                    CapToServer.levelUpAbility(e.type);
                }
            }
        }
        return false;
    }

    @Override
    public void renderInnerTooltip(PoseStack matrix, int w, int h, int mx, int my) {
        LLPlayerData handler = CapProxy.getHandler();
        for (AbilityType e : AbilityType.values()) {
            if (e.within(mx - w / 2f, my - h / 2f)) {
                int lv = e.type.level.apply(handler);
                int cost = e.cost.apply(handler.abilityPoints);
                List<FormattedText> list = new ArrayList<>();
                list.add(e.getDesc());
                list.add(LangData.IDS.GUI_ABILITY_LV.get(lv));
                list.add(LangData.IDS.GUI_ABILITY_COST.get(1, cost));
                LangData.IDS lvup = e.type.checkLevelUp(handler);
                if (lvup != null)
                    list.add(lvup.get().withStyle(ChatFormatting.RED));
                renderTooltip(matrix, Language.getInstance().getVisualOrder(list), mx, my);
            }
        }
    }

    public enum AbilityType {
        HEALTH(AbilityPoints.LevelType.HEALTH, e -> e.general + e.body, "health", -H_DIS, -Y_DIS),
        STRENGTH(AbilityPoints.LevelType.STRENGTH, e -> e.general + e.body, "strength", -H_DIS, 0),
        SPEED(AbilityPoints.LevelType.SPEED, e -> e.general + e.body, "speed", -H_DIS, Y_DIS),
        MAGIC(AbilityPoints.LevelType.MANA, e -> e.general + e.magic, "magic", H_DIS, -Y_DIS),
        SPELL(AbilityPoints.LevelType.SPELL, e -> e.general + e.magic, "spell", H_DIS, 0);

        public final AbilityPoints.LevelType type;
        public final Function<AbilityPoints, Integer> cost;
        public final String icon;
        public final int x, y;

        AbilityType(AbilityPoints.LevelType type, Function<AbilityPoints, Integer> cost, String icon, int x, int y) {
            this.type = type;
            this.cost = cost;
            this.icon = icon;
            this.x = x;
            this.y = y;
        }

        public void render(LLPlayerData handler, PoseStack matrix, int mx, int my) {
            RenderSystem.setShaderTexture(0, new ResourceLocation(LightLand.MODID, "textures/ability/" + icon + ".png"));
            AbstractHexGui.drawScaled(matrix, x, y, 2);
            if (within(mx, my) && type.checkLevelUp(CapProxy.getHandler()) == null)
                fill(matrix, x - 8, y - 8, x + 8, y + 8, 0x80FFFFFF);
        }

        public boolean within(double mx, double my) {
            return mx > x - 8 && mx < x + 8 && my > y - 8 && my < y + 8;
        }

        public Component getDesc() {
            return LangData.get(this);
        }

    }

}
