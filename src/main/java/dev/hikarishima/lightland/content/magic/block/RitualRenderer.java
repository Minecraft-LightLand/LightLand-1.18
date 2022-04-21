package dev.hikarishima.lightland.content.magic.block;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.lcy0x1.util.RenderUtils;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.ParametersAreNonnullByDefault;

@OnlyIn(Dist.CLIENT)
@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class RitualRenderer implements BlockEntityRenderer<RitualTE> {

	public RitualRenderer(BlockEntityRendererProvider.Context dispatcher) {

	}

	@Override
	public void render(RitualTE te, float partial, PoseStack matrix, MultiBufferSource buffer, int light, int overlay) {
		RenderUtils.renderItemAbove(te.getItem(0), 1.5, te.getLevel(), partial, matrix, buffer, light, overlay);
	}

}
