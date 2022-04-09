package dev.hikarishima.lightland.content.common.test;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.hikarishima.lightland.content.magic.block.RitualRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import software.bernie.geckolib3.renderers.geo.GeoBlockRenderer;

public class PanBlockEntityRenderer extends GeoBlockRenderer<PanBlockEntity> {

	public PanBlockEntityRenderer(BlockEntityRendererProvider.Context rendererDispatcherIn) {
		super(rendererDispatcherIn, new PanGeoModel());
	}

	@Override
	public void render(BlockEntity tile, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int combinedLightIn, int combinedOverlayIn) {
		super.render(tile, partialTicks, matrixStackIn, bufferIn, combinedLightIn, combinedOverlayIn);
		ItemStack result = ((PanBlockEntity) tile).outputInventory.getItem(0);
		RitualRenderer.renderItemAbove(result, tile.getLevel(), partialTicks, matrixStackIn, bufferIn, combinedLightIn, combinedOverlayIn);
	}

}
