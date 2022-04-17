package dev.xkmc.cuisine.content.tools.mill;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.level.block.entity.BlockEntity;
import software.bernie.geckolib3.renderers.geo.GeoBlockRenderer;

public class MillRenderer extends GeoBlockRenderer<MillBlockEntity> {

	public MillRenderer(BlockEntityRendererProvider.Context rendererDispatcherIn) {
		super(rendererDispatcherIn, new MillGeoModel());
	}

	@Override
	public void render(BlockEntity tile, float ptick, PoseStack pose, MultiBufferSource buffer, int light, int overlay) {
		super.render(tile, ptick, pose, buffer, light, overlay);
	}

}
