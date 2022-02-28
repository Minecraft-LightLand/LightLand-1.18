package dev.hikarishima.lightland.content.common.gui.ability;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import dev.hikarishima.lightland.content.common.gui.GuiTabType;
import dev.hikarishima.lightland.init.registrate.ItemRegistrate;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.function.BooleanSupplier;
import java.util.function.Supplier;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public abstract class AbstractAbilityScreen extends Screen {

    private static final ResourceLocation WINDOW_LOCATION = new ResourceLocation("textures/gui/advancements/window.png");
    private static final ResourceLocation TABS_LOCATION = new ResourceLocation("textures/gui/advancements/tabs.png");

    protected final AbilityTab tab;

    protected AbstractAbilityScreen(AbilityTab tab, Component title) {
        super(title);
        this.tab = tab;
    }

    @Override
    public final void render(PoseStack matrix, int mx, int my, float partial) {
        int x0 = (this.width - 252) / 2;
        int y0 = (this.height - 140) / 2;
        renderBackground(matrix);
        matrix.pushPose();
        matrix.translate((float) (x0 + 9), (float) (y0 + 18), 0.0F);
        renderInside(matrix, 234, 113, mx - x0 - 9, my - y0 - 18, partial);
        matrix.popPose();
        RenderSystem.depthFunc(515);
        RenderSystem.disableDepthTest();
        TextureManager tm = Minecraft.getInstance().getTextureManager();
        ItemRenderer ir = Minecraft.getInstance().getItemRenderer();
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.enableBlend();
        RenderSystem.setShaderTexture(0, WINDOW_LOCATION);
        this.blit(matrix, x0, y0, 0, 0, 252, 140);
        RenderSystem.setShaderTexture(0, TABS_LOCATION);
        for (AbilityTab tab : AbilityTab.values()) {
            tab.type.draw(matrix, this, x0, y0, this.tab == tab, tab.index);
        }
        RenderSystem.defaultBlendFunc();
        for (AbilityTab tab : AbilityTab.values()) {
            tab.type.drawIcon(x0, y0, tab.index, ir, tab.icon.get());
        }
        RenderSystem.disableBlend();
        this.font.draw(matrix, tab.title, (float) (x0 + 8), (float) (y0 + 6), 4210752);
        for (AbilityTab tab : AbilityTab.values()) {
            if (tab.type.isMouseOver(x0, y0, tab.index, mx, my)) {
                renderTooltip(matrix, tab.title, mx, my);
            }
        }
        matrix.pushPose();
        matrix.translate((float) (x0 + 9), (float) (y0 + 18), 0.0F);
        renderInnerTooltip(matrix, 234, 113, mx - x0 - 9, my - y0 - 18);
        matrix.popPose();
    }

    protected abstract void renderInside(PoseStack matrix, int w, int h, int mx, int my, float partial);

    @Override
    public boolean mouseClicked(double mx, double my, int button) {
        int x0 = (this.width - 252) / 2;
        int y0 = (this.height - 140) / 2;
        if (button == 0) {
            for (AbilityTab tab : AbilityTab.values()) {
                if (tab != this.tab && tab.type.isMouseOver(x0, y0, tab.index, mx, my) && tab.pred.getAsBoolean()) {
                    Minecraft.getInstance().setScreen(tab.sup.get());
                    return true;
                }
            }
            if (innerMouseClick(234, 113, mx - x0 - 9, my - y0 - 18))
                return true;
        }
        return super.mouseClicked(mx, my, button);
    }

    public abstract boolean innerMouseClick(int w, int h, double mx, double my);

    public abstract void renderInnerTooltip(PoseStack matrix, int w, int h, int mx, int my);

    public enum AbilityTab {
        PROFESSION(0, Items.IRON_SWORD::getDefaultInstance, ProfessionScreen::canAccess, ProfessionScreen::new, ProfessionScreen.TITLE),
        ABILITY(1, Items.GOLDEN_APPLE::getDefaultInstance, () -> true, AbilityScreen::new, AbilityScreen.TITLE),
        ELEMENT(2, () -> ItemRegistrate.MAGIC_WAND.get().getDefaultInstance(), ElementalScreen::canAccess, ElementalScreen::new, ElementalScreen.TITLE),
        ARCANE(3, () -> ItemRegistrate.ARCANE_AXE_GILDED.get().getDefaultInstance(), ArcaneScreen::canAccess, ArcaneScreen::new, ArcaneScreen.TITLE);

        public final GuiTabType type = GuiTabType.ABOVE;
        public final int index;
        public final Supplier<ItemStack> icon;
        public final BooleanSupplier pred;
        public final Supplier<? extends AbstractAbilityScreen> sup;
        public final Component title;

        AbilityTab(int index, Supplier<ItemStack> icon, BooleanSupplier pred, Supplier<? extends AbstractAbilityScreen> sup, Component title) {
            this.index = index;
            this.icon = icon;
            this.pred = pred;
            this.sup = sup;
            this.title = title;
        }
    }

}
