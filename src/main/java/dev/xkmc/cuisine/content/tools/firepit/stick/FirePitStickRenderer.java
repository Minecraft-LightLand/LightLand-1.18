package dev.xkmc.cuisine.content.tools.firepit.stick;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.lcy0x1.util.RenderUtils;
import dev.xkmc.cuisine.content.tools.base.ContentRenderer;
import dev.xkmc.cuisine.content.tools.base.tile.TileInfoOverlay;
import dev.xkmc.cuisine.content.tools.pan.PanBlockEntity;
import dev.xkmc.cuisine.content.tools.pan.PanGeoModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

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
