package dev.xkmc.cuisine.content.tools.pan;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.xkmc.l2library.util.RenderUtils;
import dev.xkmc.cuisine.content.tools.base.ContentRenderer;
import dev.xkmc.cuisine.content.tools.base.tile.TileInfoOverlay;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import software.bernie.geckolib3.renderers.geo.GeoBlockRenderer;

public class PanRenderer extends GeoBlockRenderer<PanBlockEntity> {

	public PanRenderer(BlockEntityRendererProvider.Context rendererDispatcherIn) {
		super(rendererDispatcherIn, new PanGeoModel());
	}

	@Override
	public void render(BlockEntity tile, float ptick, PoseStack pose, MultiBufferSource buffer, int light, int overlay) {
		super.render(tile, ptick, pose, buffer, light, overlay);
		PanBlockEntity te = (PanBlockEntity) tile;
		ItemStack result = te.getResult();
		if (tile.getLevel() != null && !result.isEmpty() && te.getBlockState().getValue(BlockStateProperties.OPEN)) {
			RenderUtils.renderItemAbove(result, 0.7, tile.getLevel(), ptick, pose, buffer, light, overlay);
		}
		ContentRenderer.renderBasinContent(new ContentRenderer.Context(pose, buffer, light, overlay, ptick,
				3.5f / 16f, 12.5f / 16f, 5 / 16f, 10 / 16f - 1e-3f, 3.5f / 16f, 12.5f / 16f,
				0.125f, 0.5f,true,
				(TileInfoOverlay.TileInfoProvider) tile, tile.getBlockPos(), tile.getLevel(), PanBlockEntity.MAX_FLUID * 4));
	}

}
