package dev.xkmc.cuisine.content.tools.firepit.wok;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.xkmc.l2library.util.RenderUtils;
import dev.xkmc.cuisine.content.tools.base.ContentRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.item.ItemStack;

public class FirePitWokRenderer implements BlockEntityRenderer<FirePitWokBlockEntity> {

	public FirePitWokRenderer(BlockEntityRendererProvider.Context ctx) {
	}

	@Override
	public void render(FirePitWokBlockEntity tile, float pTick, PoseStack poseStack, MultiBufferSource source, int light, int overlay) {
		ItemStack result = tile.getResult();
		if (tile.getLevel() != null && !result.isEmpty()) {
			RenderUtils.renderItemAbove(result, 6 / 16f, tile.getLevel(), pTick, poseStack, source, light, overlay);
		}
		ContentRenderer.renderBasinContent(new ContentRenderer.Context(poseStack, source, light, overlay, pTick,
				1.5f / 16f, 14.5f / 16f, 3 / 16f, 7 / 16f, 1.5f / 16f, 14.5f / 16f,
				0.125f, 0.5f, true, tile, tile.getBlockPos(), tile.getLevel(), FirePitWokBlockEntity.MAX_FLUID * 4));
	}

}
