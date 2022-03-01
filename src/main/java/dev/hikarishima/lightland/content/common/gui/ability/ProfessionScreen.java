package dev.hikarishima.lightland.content.common.gui.ability;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.tterrag.registrate.util.entry.RegistryEntry;
import dev.hikarishima.lightland.content.common.capability.CapProxy;
import dev.hikarishima.lightland.content.common.capability.LLPlayerData;
import dev.hikarishima.lightland.content.magic.gui.AbstractHexGui;
import dev.hikarishima.lightland.content.profession.Profession;
import dev.hikarishima.lightland.init.data.LangData;
import dev.hikarishima.lightland.init.special.LightLandRegistry;
import dev.hikarishima.lightland.network.packets.CapToServer;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.locale.Language;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.List;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class ProfessionScreen extends AbstractAbilityScreen {

    public static final Component TITLE = LangData.IDS.GUI_PROF.get();
    private static final int H_DIS = 15, Y_DIS = 30;

    public static boolean canAccess() {
        return true;
    }

    public ProfessionScreen() {
        super(AbilityTab.PROFESSION, TITLE);
    }

    @Override
    protected void renderInside(PoseStack matrix, int w, int h, int mx, int my, float partial) {
        fill(matrix, 0, 0, w, h, 0xFF606060);
        matrix.pushPose();
        matrix.translate(w / 2f, h / 2f, 0);
        mx -= w / 2;
        my -= h / 2;
        LLPlayerData handler = CapProxy.getHandler();
        for (ProfType e : ProfType.values()) {
            e.render(handler, matrix, mx, my);
        }
        matrix.popPose();
    }

    @Override
    public boolean innerMouseClick(int w, int h, double mx, double my) {
        LLPlayerData handler = CapProxy.getHandler();
        if (!canAccess())
            return false;
        for (ProfType e : ProfType.values()) {
            if (e.within(mx - w / 2f, my - h / 2f)) {
                if (handler.abilityPoints.setProfession(e.prof.get())) {
                    CapToServer.setProfession(e.prof.get());
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void renderInnerTooltip(PoseStack matrix, int w, int h, int mx, int my) {
        LLPlayerData handler = CapProxy.getHandler();
        for (ProfType e : ProfType.values()) {
            if (e.within(mx - w / 2f, my - h / 2f)) {
                List<FormattedText> list = new ArrayList<>();
                list.add(e.prof.get().getDesc());
                if (handler.abilityPoints.profession != null)
                    list.add(LangData.IDS.GUI_PROF_EXIST.get());
                renderTooltip(matrix, Language.getInstance().getVisualOrder(list), mx, my);
            }
        }
    }

    public enum ProfType {
        MAGICIAN(-3 * H_DIS, -Y_DIS, LightLandRegistry.PROF_MAGIC),
        SPELL_CASTER(-3 * H_DIS, 0, LightLandRegistry.PROF_SPELL),
        ARCANE(-3 * H_DIS, Y_DIS, LightLandRegistry.PROF_ARCANE),
        KNIGHT(-H_DIS, -Y_DIS, LightLandRegistry.PROF_KNIGHT),
        BURSERKER(-H_DIS, 0, LightLandRegistry.PROF_BURSERKER),
        SHIELDER(-H_DIS, Y_DIS, LightLandRegistry.PROF_SHIELDER),
        ARCHER(H_DIS, -Y_DIS, LightLandRegistry.PROF_ARCHER),
        HUNTER(H_DIS, 0, LightLandRegistry.PROF_HUNTER),
        ASSASSIN(H_DIS, Y_DIS, LightLandRegistry.PROF_ASSASSIN),
        ALCHEMIST(3 * H_DIS, -Y_DIS, LightLandRegistry.PROF_ALCHEM),
        CHEMIST(3 * H_DIS, 0, LightLandRegistry.PROF_CHEM),
        TIDECALLER(3 * H_DIS, Y_DIS, LightLandRegistry.PROF_TIDE);

        public final int x, y;
        public final RegistryEntry<? extends Profession> prof;

        ProfType(int x, int y, RegistryEntry<? extends Profession> prof) {
            this.x = x;
            this.y = y;
            this.prof = prof;
        }

        public void render(LLPlayerData handler, PoseStack matrix, int mx, int my) {
            RenderSystem.setShaderTexture(0, prof.get().getIcon());
            AbstractHexGui.drawScaled(matrix, x, y, 2);
            if (within(mx, my) && handler.abilityPoints.profession == null)
                fill(matrix, x - 8, y - 8, x + 8, y + 8, 0x80FFFFFF);
        }

        public boolean within(double mx, double my) {
            return mx > x - 8 && mx < x + 8 && my > y - 8 && my < y + 8;
        }

    }

}
