package dev.xkmc.cuisine.content.tools.base.tile;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Matrix4f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.client.gui.ForgeIngameGui;
import net.minecraftforge.client.gui.GuiUtils;
import net.minecraftforge.client.gui.IIngameOverlay;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.FluidStack;

import java.util.List;

public class TileInfoOverlay implements IIngameOverlay {

	public interface IDrawable {

		void render(PoseStack poseStack);

	}

	public record ItemDrawable(ItemStack stack) implements IDrawable {

		public void render(PoseStack matrixStack) {
			PoseStack modelViewStack = RenderSystem.getModelViewStack();
			modelViewStack.pushPose();
			modelViewStack.mulPoseMatrix(matrixStack.last().pose());
			RenderSystem.enableDepthTest();
			Minecraft minecraft = Minecraft.getInstance();
			ItemRenderer itemRenderer = minecraft.getItemRenderer();
			itemRenderer.renderAndDecorateFakeItem(stack, 0, 0);
			itemRenderer.renderGuiItemDecorations(minecraft.font, stack, 0, 0);
			RenderSystem.disableBlend();
			modelViewStack.popPose();
			RenderSystem.applyModelViewMatrix();
		}

	}

	public record FluidDrawable(FluidStack stack, int cap, int gran) implements IDrawable {

		private static final int TEXTURE_SIZE = 16;
		private static final int MIN_FLUID_HEIGHT = 1;

		@Override
		public void render(PoseStack poseStack) {
			RenderSystem.enableBlend();
			drawFluid(poseStack, 16, 16, stack);
			RenderSystem.setShaderColor(1, 1, 1, 1);
			RenderSystem.disableBlend();
			if (stack.getAmount() % gran == 0) {
				String s = String.valueOf(stack.getAmount() / gran);
				Minecraft minecraft = Minecraft.getInstance();
				ItemRenderer itemRenderer = minecraft.getItemRenderer();
				poseStack.translate(0.0D, 0.0D, itemRenderer.blitOffset + 200.0F);
				MultiBufferSource.BufferSource source = MultiBufferSource.immediate(Tesselator.getInstance().getBuilder());
				minecraft.font.drawInBatch(s, (float) (19 - 2 - minecraft.font.width(s)), (float) (6 + 3), 16777215,
						true, poseStack.last().pose(), source, false, 0, 15728880);
				source.endBatch();
			}
		}

		private void drawFluid(PoseStack poseStack, final int width, final int height, FluidStack fluidStack) {
			Fluid fluid = fluidStack.getFluid();
			if (fluid == null) {
				return;
			}

			TextureAtlasSprite fluidStillSprite = getStillFluidSprite(fluidStack);

			FluidAttributes attributes = fluid.getAttributes();
			int fluidColor = attributes.getColor(fluidStack);

			int amount = fluidStack.getAmount();
			int scaledAmount = (amount * height) / cap;
			if (amount > 0 && scaledAmount < MIN_FLUID_HEIGHT) {
				scaledAmount = MIN_FLUID_HEIGHT;
			}
			if (scaledAmount > height) {
				scaledAmount = height;
			}

			drawTiledSprite(poseStack, width, height, fluidColor, scaledAmount, fluidStillSprite);
		}

		private static void drawTiledSprite(PoseStack poseStack, final int tiledWidth, final int tiledHeight, int color, int scaledAmount, TextureAtlasSprite sprite) {
			RenderSystem.setShaderTexture(0, InventoryMenu.BLOCK_ATLAS);
			Matrix4f matrix = poseStack.last().pose();
			setGLColorFromInt(color);

			final int xTileCount = tiledWidth / TEXTURE_SIZE;
			final int xRemainder = tiledWidth - (xTileCount * TEXTURE_SIZE);
			final int yTileCount = scaledAmount / TEXTURE_SIZE;
			final int yRemainder = scaledAmount - (yTileCount * TEXTURE_SIZE);

			final int yStart = tiledHeight;

			for (int xTile = 0; xTile <= xTileCount; xTile++) {
				for (int yTile = 0; yTile <= yTileCount; yTile++) {
					int width = (xTile == xTileCount) ? xRemainder : TEXTURE_SIZE;
					int height = (yTile == yTileCount) ? yRemainder : TEXTURE_SIZE;
					int x = (xTile * TEXTURE_SIZE);
					int y = yStart - ((yTile + 1) * TEXTURE_SIZE);
					if (width > 0 && height > 0) {
						int maskTop = TEXTURE_SIZE - height;
						int maskRight = TEXTURE_SIZE - width;

						drawTextureWithMasking(matrix, x, y, sprite, maskTop, maskRight, 100);
					}
				}
			}
		}

		private static TextureAtlasSprite getStillFluidSprite(FluidStack fluidStack) {
			Minecraft minecraft = Minecraft.getInstance();
			Fluid fluid = fluidStack.getFluid();
			FluidAttributes attributes = fluid.getAttributes();
			ResourceLocation fluidStill = attributes.getStillTexture(fluidStack);
			return minecraft.getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(fluidStill);
		}

		private static void setGLColorFromInt(int color) {
			float red = (color >> 16 & 0xFF) / 255.0F;
			float green = (color >> 8 & 0xFF) / 255.0F;
			float blue = (color & 0xFF) / 255.0F;
			float alpha = ((color >> 24) & 0xFF) / 255F;

			RenderSystem.setShaderColor(red, green, blue, alpha);
		}

		private static void drawTextureWithMasking(Matrix4f matrix, float xCoord, float yCoord, TextureAtlasSprite textureSprite, int maskTop, int maskRight, float zLevel) {
			float uMin = textureSprite.getU0();
			float uMax = textureSprite.getU1();
			float vMin = textureSprite.getV0();
			float vMax = textureSprite.getV1();
			uMax = uMax - (maskRight / 16F * (uMax - uMin));
			vMax = vMax - (maskTop / 16F * (vMax - vMin));

			RenderSystem.setShader(GameRenderer::getPositionTexShader);

			Tesselator tessellator = Tesselator.getInstance();
			BufferBuilder bufferBuilder = tessellator.getBuilder();
			bufferBuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
			bufferBuilder.vertex(matrix, xCoord, yCoord + 16, zLevel).uv(uMin, vMax).endVertex();
			bufferBuilder.vertex(matrix, xCoord + 16 - maskRight, yCoord + 16, zLevel).uv(uMax, vMax).endVertex();
			bufferBuilder.vertex(matrix, xCoord + 16 - maskRight, yCoord + maskTop, zLevel).uv(uMax, vMin).endVertex();
			bufferBuilder.vertex(matrix, xCoord, yCoord + maskTop, zLevel).uv(uMin, vMin).endVertex();
			tessellator.end();
		}

	}

	public interface TileInfoProvider {

		List<IDrawable> getContents();

	}

	@Override
	public void render(ForgeIngameGui gui, PoseStack poseStack, float partialTick, int width, int height) {
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		HitResult objectMouseOver = Minecraft.getInstance().hitResult;
		if (!(objectMouseOver instanceof BlockHitResult result)) {
			return;
		}
		Minecraft mc = Minecraft.getInstance();
		ClientLevel world = mc.level;
		if (world == null) return;
		BlockPos pos = result.getBlockPos();
		BlockEntity te = world.getBlockEntity(pos);
		if (!(te instanceof TileInfoProvider provider)) return;
		List<IDrawable> list = provider.getContents();
		if (list.size() == 0) return;
		int row_n = (int) Math.max(Math.min(list.size(), 6), Math.ceil(Math.sqrt(list.size())));
		int col_n = (int) Math.ceil(1.0 * list.size() / row_n);
		int spacing = 18;
		int x_off = 12;
		int y_off = -12;
		int margin = 0;
		drawHoveringText(poseStack, width / 2 + x_off, height / 2 + y_off,
				margin * 2 + row_n * spacing, margin * 2 + col_n * spacing);
		for (int i = 0; i < list.size(); i++) {
			poseStack.pushPose();
			int x = width / 2 + x_off + margin + 1 + i % row_n * spacing;
			int y = height / 2 + y_off + margin + 1 + i / row_n * spacing;
			poseStack.translate(x, y, 0);
			list.get(i).render(poseStack);
			poseStack.popPose();
		}
	}


	public static void drawHoveringText(PoseStack pStack, int x, int y, int w, int h) {
		int backgroundColor = 0x80100010;
		int borderColorStart = 0xF0FFDA00;
		int borderColorEnd = 0xF0FFDA00;
		RenderSystem.disableDepthTest();
		final int zLevel = -400;
		pStack.pushPose();
		Matrix4f mat = pStack.last()
				.pose();
		GuiUtils.drawGradientRect(mat, zLevel, x - 3, y - 4, x + w + 3,
				y - 3, backgroundColor, backgroundColor);
		GuiUtils.drawGradientRect(mat, zLevel, x - 3, y + h + 3,
				x + w + 3, y + h + 4, backgroundColor, backgroundColor);
		GuiUtils.drawGradientRect(mat, zLevel, x - 3, y - 3, x + w + 3,
				y + h + 3, backgroundColor, backgroundColor);
		GuiUtils.drawGradientRect(mat, zLevel, x - 4, y - 3, x - 3, y + h + 3,
				backgroundColor, backgroundColor);
		GuiUtils.drawGradientRect(mat, zLevel, x + w + 3, y - 3,
				x + w + 4, y + h + 3, backgroundColor, backgroundColor);
		GuiUtils.drawGradientRect(mat, zLevel, x - 3, y - 3 + 1, x - 3 + 1,
				y + h + 3 - 1, borderColorStart, borderColorEnd);
		GuiUtils.drawGradientRect(mat, zLevel, x + w + 2, y - 3 + 1,
				x + w + 3, y + h + 3 - 1, borderColorStart, borderColorEnd);
		GuiUtils.drawGradientRect(mat, zLevel, x - 3, y - 3, x + w + 3,
				y - 3 + 1, borderColorStart, borderColorStart);
		GuiUtils.drawGradientRect(mat, zLevel, x - 3, y + h + 2,
				x + w + 3, y + h + 3, borderColorEnd, borderColorEnd);

		MultiBufferSource.BufferSource renderType = MultiBufferSource.immediate(Tesselator.getInstance()
				.getBuilder());
		pStack.translate(0.0D, 0.0D, zLevel);

		renderType.endBatch();
		pStack.popPose();
	}

}
