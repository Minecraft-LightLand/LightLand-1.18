package dev.xkmc.cuisine.content.tools.firepit.stick;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.xkmc.l2library.util.RenderUtils;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.item.ItemStack;

public class FirePitStickRenderer implements BlockEntityRenderer<FirePitStickBlockEntity> {

	public FirePitStickRenderer(BlockEntityRendererProvider.Context ctx) {
	}

	@Override
	public void render(FirePitStickBlockEntity tile, float pTick, PoseStack poseStack, MultiBufferSource source, int light, int overlay) {
		ItemStack result = tile.getResult();
		if (tile.getLevel() != null && !result.isEmpty()) {
			RenderUtils.renderItemAbove(result, 0.7, tile.getLevel(), pTick, poseStack, source, light, overlay);
		}
	}

}
