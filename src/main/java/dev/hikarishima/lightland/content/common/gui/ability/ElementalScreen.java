package dev.hikarishima.lightland.content.common.gui.ability;

import com.hikarishima.lightland.magic.MagicElement;
import com.hikarishima.lightland.magic.MagicProxy;
import com.hikarishima.lightland.magic.MagicRegistry;
import com.hikarishima.lightland.magic.Translator;
import com.hikarishima.lightland.magic.capabilities.MagicHandler;
import com.hikarishima.lightland.magic.capabilities.ToServerMsg;
import com.hikarishima.lightland.magic.gui.AbstractHexGui;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.tterrag.registrate.util.entry.RegistryEntry;
import dev.hikarishima.lightland.content.common.capability.CapProxy;
import dev.hikarishima.lightland.content.common.capability.LLPlayerData;
import dev.hikarishima.lightland.content.magic.gui.AbstractHexGui;
import dev.hikarishima.lightland.content.magic.products.MagicElement;
import dev.hikarishima.lightland.init.special.LightLandRegistry;
import dev.hikarishima.lightland.init.special.MagicRegistry;
import dev.hikarishima.lightland.network.packets.CapToServer;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.network.chat.Component;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.ITextProperties;
import net.minecraft.util.text.LanguageMap;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.List;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class ElementalScreen extends AbstractAbilityScreen {

    public static final Component TITLE = Translator.get("screen.ability.elemental.title");
    private static final int RADIUS = 30;

    public static boolean canAccess() {
        LLPlayerData handler = CapProxy.getHandler();
        return handler.abilityPoints.canLevelElement() ||
                LightLandRegistry.ELEMENT.getValues().stream()
                        .anyMatch(e -> handler.magicHolder.getElementalMastery(e) > 0);
    }

    protected ElementalScreen() {
        super(AbilityTab.ELEMENT, TITLE);
    }

    @Override
    protected void renderInside(PoseStack matrix, int w, int h, int mx, int my, float partial) {
        fill(matrix, 0, 0, w, h, 0xFF606060);
        matrix.pushPose();
        matrix.translate(w / 2f, h / 2f, 0);
        mx -= w / 2;
        my -= h / 2;
        LLPlayerData handler = CapProxy.getHandler();
        for (ElemType e : ElemType.values()) {
            e.renderElem(handler, matrix, mx, my);
        }
        matrix.popPose();
    }

    @Override
    public boolean innerMouseClick(int w, int h, double mx, double my) {
        LLPlayerData handler = CapProxy.getHandler();
        if (!handler.abilityPoints.canLevelElement())
            return false;
        for (ElemType e : ElemType.values()) {
            if (e.within(mx - w / 2f, my - h / 2f)) {
                if (handler.magicHolder.addElementalMastery(e.elem.get())) {
                    CapToServer.addElemMastery(e.elem.get());
                    handler.abilityPoints.levelElement();
                }
            }
        }
        return false;
    }

    @Override
    public void renderInnerTooltip(PoseStack matrix, int w, int h, int mx, int my) {
        LLPlayerData handler = CapProxy.getHandler();
        for (ElemType e : ElemType.values()) {
            if (e.within(mx - w / 2f, my - h / 2f)) {
                int lv = handler.magicHolder.getElementalMastery(e.elem.get());
                int count = handler.magicHolder.getElement(e.elem.get());
                int rem = handler.abilityPoints.element;
                List<Component> list = new ArrayList<>();
                list.add(e.elem.get().getDesc());
                list.add(Translator.get("screen.ability.elemental.desc.lv", lv));
                list.add(Translator.get("screen.ability.elemental.desc.count", count));
                list.add(Translator.get("screen.ability.elemental.desc.cost", 1, rem));
                renderTooltip(matrix, LanguageMap.getInstance().getVisualOrder(list), mx, my);
            }
        }
    }

    public enum ElemType {
        E(0, RADIUS, MagicRegistry.ELEM_EARTH),
        W(-RADIUS, 0, MagicRegistry.ELEM_WATER),
        A(0, -RADIUS, MagicRegistry.ELEM_AIR),
        F(RADIUS, 0, MagicRegistry.ELEM_FIRE),
        Q(0, 0, MagicRegistry.ELEM_QUINT);

        public final int x, y;
        public final RegistryEntry<MagicElement> elem;

        ElemType(int x, int y, RegistryEntry<MagicElement> elem) {
            this.x = x;
            this.y = y;
            this.elem = elem;
        }

        public void renderElem(LLPlayerData handler, PoseStack matrix, int mx, int my) {
            int lv = handler.magicHolder.getElementalMastery(elem.get());
            int count = handler.magicHolder.getElement(elem.get());
            AbstractHexGui.drawElement(matrix, x, y, elem.get(), "" + lv);
            if (within(mx, my) && handler.abilityPoints.canLevelElement())
                fill(matrix, x - 8, y - 8, x + 8, y + 8, 0x80FFFFFF);
        }

        public boolean within(double mx, double my) {
            return mx > x - 8 && mx < x + 8 && my > y - 8 && my < y + 8;
        }

    }

}
