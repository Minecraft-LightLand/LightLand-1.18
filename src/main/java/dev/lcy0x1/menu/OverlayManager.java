package dev.lcy0x1.menu;

import com.google.gson.JsonObject;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import dev.lcy0x1.serial.ExceptionHandler;
import dev.lcy0x1.serial.SerialClass;
import dev.lcy0x1.serial.Serializer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

@SerialClass
public class OverlayManager {

	private final String name;
	private final ResourceLocation coords, texture;
	@SerialClass.SerialField
	public HashMap<String, Rect> elements;
	private boolean loaded = false;

	public OverlayManager(String mod, String str) {
		name = mod + ":" + str;
		coords = new ResourceLocation(mod, str);
		texture = new ResourceLocation(mod, "textures/gui/overlays/" + str + ".png");
		check();
	}

	/**
	 * get the location of the component on the GUI
	 */
	public Rect getComp(String key) {
		check();
		return elements.getOrDefault(key, Rect.ZERO);
	}

	@OnlyIn(Dist.CLIENT)
	public ScreenRenderer getRenderer(Screen gui, int x, int y, int w, int h) {
		check();
		return new ScreenRenderer(gui, x, y, w, h);
	}

	@Override
	public String toString() {
		return name;
	}

	private void check() {
		if (!loaded)
			load();
	}

	private void load() {
		JsonObject jo = ExceptionHandler.get(() -> GsonHelper.parse(new InputStreamReader(Minecraft.getInstance().getResourceManager().getResource(
				new ResourceLocation(coords.getNamespace(), "textures/gui/overlays/" + coords.getPath() + ".json")
		).getInputStream(), StandardCharsets.UTF_8)));
		Serializer.from(jo, OverlayManager.class, this);
		loaded = true;
	}

	@SerialClass
	public static class Rect {

		public static final Rect ZERO = new Rect();

		@SerialClass.SerialField
		public int sx, sy, tx, ty, w, h;

		public Rect() {
		}

	}

	@OnlyIn(Dist.CLIENT)
	public class ScreenRenderer {

		private final int x, y, w, h;
		private final Screen scr;

		public ScreenRenderer(Screen gui, int x, int y, int w, int h) {
			scr = gui;
			this.x = x;
			this.y = y;
			this.w = w;
			this.h = h;
		}

		private ScreenRenderer(AbstractContainerScreen<?> scrIn) {
			x = scrIn.getGuiLeft();
			y = scrIn.getGuiTop();
			w = scrIn.getXSize();
			h = scrIn.getYSize();
			scr = scrIn;
		}

		/**
		 * Draw a side sprite on the location specified by the component
		 */
		public void draw(PoseStack mat, String c, String s) {
			Rect cr = getComp(c);
			scr.blit(mat, x + cr.sx, y + cr.sy, cr.tx, cr.ty, cr.w, cr.h);
		}

		/**
		 * Draw a side sprite on the location specified by the component. Draw partially
		 * from bottom to top
		 */
		public void drawBottomUp(PoseStack mat, String c, String s, int prog, int max) {
			if (prog == 0 || max == 0)
				return;
			Rect cr = getComp(c);
			int dh = cr.h * prog / max;
			scr.blit(mat, x + cr.sx, y + cr.sy + cr.h - dh, cr.tx, cr.ty + cr.h - dh, cr.w, dh);
		}

		/**
		 * Draw a side sprite on the location specified by the component. Draw partially
		 * from left to right
		 */
		public void drawLeftRight(PoseStack mat, String c, String s, int prog, int max) {
			if (prog == 0 || max == 0)
				return;
			Rect cr = getComp(c);
			int dw = cr.w * prog / max;
			scr.blit(mat, x + cr.sx, y + cr.sy, cr.tx, cr.ty, dw, cr.h);
		}

		/**
		 * bind texture, draw background color, and GUI background
		 */
		public void start(PoseStack mat) {
			RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
			RenderSystem.setShaderTexture(0, texture);
			scr.blit(mat, x, y, 0, 0, w, h);
		}

	}

}
