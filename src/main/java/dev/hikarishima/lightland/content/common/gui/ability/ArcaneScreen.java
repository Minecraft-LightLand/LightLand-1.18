package dev.hikarishima.lightland.content.common.gui.ability;

import com.hikarishima.lightland.magic.Translator;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.tterrag.registrate.util.entry.RegistryEntry;
import dev.hikarishima.lightland.content.arcane.internal.ArcaneType;
import dev.hikarishima.lightland.content.common.capability.CapProxy;
import dev.hikarishima.lightland.content.common.capability.LLPlayerData;
import dev.hikarishima.lightland.content.magic.gui.AbstractHexGui;
import dev.hikarishima.lightland.init.special.LightLandRegistry;
import dev.hikarishima.lightland.network.packets.CapToServer;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.advancements.FrameType;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.util.text.LanguageMap;
import net.minecraft.world.item.ItemStack;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.List;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class ArcaneScreen extends AbstractAbilityScreen {

    public static final Component TITLE = Translator.get("screen.ability.arcane.title");

    public static boolean canAccess() {
        LLPlayerData handler = CapProxy.getHandler();
        return handler.abilityPoints.canLevelArcane() ||
                LightLandRegistry.ARCANE_TYPE.getValues().stream()
                        .anyMatch(handler.magicAbility::isArcaneTypeUnlocked);
    }

    protected ArcaneScreen() {
        super(AbilityTab.ARCANE, TITLE);
    }

    @Override
    protected void renderInside(PoseStack matrix, int w, int h, int mx, int my, float partial) {
        fill(matrix, 0, 0, w, h, 0xFF606060);
        matrix.pushPose();
        matrix.translate(w / 2f, h / 2f, 0);
        mx -= w / 2;
        my -= h / 2;
        LLPlayerData handler = CapProxy.getHandler();
        for (ArcaneEntry e : ArcaneEntry.values()) {
            e.render(handler, matrix, mx, my);
        }
        matrix.popPose();
    }

    @Override
    public boolean innerMouseClick(int w, int h, double mx, double my) {
        LLPlayerData handler = CapProxy.getHandler();
        if (!canAccess())
            return false;
        for (ArcaneEntry e : ArcaneEntry.values()) {
            if (e.within(mx - w / 2f, my - h / 2f)) {
                if (handler.abilityPoints.canLevelArcane()) {
                    handler.magicAbility.unlockArcaneType(e.type.get(), false);
                    CapToServer.unlockArcaneType(e.type.get());
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void renderInnerTooltip(PoseStack matrix, int w, int h, int mx, int my) {
        LLPlayerData handler = CapProxy.getHandler();
        for (ArcaneEntry e : ArcaneEntry.values()) {
            if (e.within(mx - w / 2f, my - h / 2f)) {
                int cost = handler.abilityPoints.arcane + handler.abilityPoints.magic + handler.abilityPoints.general;
                List<Component> list = new ArrayList<>();
                list.add(e.type.get().getDesc());
                if (!handler.magicAbility.isArcaneTypeUnlocked(e.type.get())) {
                    list.add(Translator.get("screen.ability.arcane.cost", 1, cost));
                }
                list.add(Translator.get("screen.ability.arcane.activate." + e.type.get().hit.name().toLowerCase()));
                renderTooltip(matrix, LanguageMap.getInstance().getVisualOrder(list), mx, my);
            }
        }
    }

    public enum ArcaneEntry {
        ALKAID(ArcaneType.ALKAID, -60, -15),
        MIZAR(ArcaneType.MIZAR, -30, -15),
        ALIOTH(ArcaneType.ALIOTH, 0, -15),
        MEGERZ(ArcaneType.MEGREZ, 30, -15),
        PHECDA(ArcaneType.PHECDA, 30, 15),
        MERAK(ArcaneType.MERAK, 60, 15),
        DUBHE(ArcaneType.DUBHE, 60, -15);

        public final RegistryEntry<ArcaneType> type;
        public final int x, y;


        ArcaneEntry(RegistryEntry<ArcaneType> type, int x, int y) {
            this.type = type;
            this.x = x;
            this.y = y;
        }


        public void render(LLPlayerData handler, PoseStack matrix, int mx, int my) {
            boolean unlocked = handler.magicAbility.isArcaneTypeUnlocked(type.get());
            boolean hover = within(mx, my) && !unlocked;
            AbstractHexGui.drawFrame(matrix, unlocked ? FrameType.CHALLENGE : FrameType.TASK, hover, x, y);
            ItemStack stack = type.get().getStack();
            Minecraft.getInstance().getItemRenderer().renderAndDecorateFakeItem(stack, x - 8, y - 8);
        }

        public boolean within(double mx, double my) {
            return mx > x - 8 && mx < x + 8 && my > y - 8 && my < y + 8;
        }


    }

}
