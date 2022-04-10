package dev.hikarishima.lightland.content.secondary.pan;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.hikarishima.lightland.content.magic.block.RitualRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import software.bernie.geckolib3.renderers.geo.GeoBlockRenderer;

public class PanBlockEntityRenderer extends GeoBlockRenderer<PanBlockEntity> {

	public PanBlockEntityRenderer(BlockEntityRendererProvider.Context rendererDispatcherIn) {
		super(rendererDispatcherIn, new PanGeoModel());
	}

	@Override
	public void render(BlockEntity tile, float ptick, PoseStack pose, MultiBufferSource buffer, int light, int overlay) {
		super.render(tile, ptick, pose, buffer, light, overlay);
		PanBlockEntity te = (PanBlockEntity) tile;
		ItemStack result = te.outputInventory.getItem(0);
		if (tile.getLevel() != null && !result.isEmpty() && te.getBlockState().getValue(BlockStateProperties.OPEN)) {
			RitualRenderer.renderItemAbove(result, 0.7, tile.getLevel(), ptick, pose, buffer, light, overlay);
		}
	}

}
