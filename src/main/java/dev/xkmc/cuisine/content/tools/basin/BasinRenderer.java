package dev.xkmc.cuisine.content.tools.basin;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.xkmc.cuisine.content.tools.base.ContentRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;

public class BasinRenderer implements BlockEntityRenderer<BasinBlockEntity> {

	public BasinRenderer(BlockEntityRendererProvider.Context ctx) {

	}

	@Override
	public void render(BasinBlockEntity basin, float partialTicks, PoseStack ms, MultiBufferSource buffer,
					   int light, int overlay) {
		ContentRenderer.renderBasinContent(new ContentRenderer.Context(ms, buffer, light, overlay, partialTicks,
				1 / 16f, 15 / 16f, 1 / 16f, 7 / 16f, 1 / 16f, 15 / 16f,
				0.125f, 0.8f, true,
				basin, basin.getBlockPos(), basin.getLevel(), BasinBlockEntity.MAX_FLUID));
	}

}
