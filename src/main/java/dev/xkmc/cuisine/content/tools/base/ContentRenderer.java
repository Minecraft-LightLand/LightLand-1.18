package dev.xkmc.cuisine.content.tools.base;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import com.simibubi.create.foundation.fluid.FluidRenderer;
import com.simibubi.create.foundation.utility.AnimationTickHolder;
import com.simibubi.create.foundation.utility.VecHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.fluids.FluidStack;

import java.util.List;
import java.util.Random;

public class ContentRenderer {

	public record Context(PoseStack ms, MultiBufferSource buffer, int light, int overlay, float partialTicks,
						  float xMin, float xMax, float yMin, float yMax, float zMin, float zMax, float radius,
						  float scale,
						  TileInfoOverlay.TileInfoProvider tile, BlockPos pos, Level level, int max_fluid
	) {

	}

	public static void renderContent(Context ctx) {
		float fluidLevel = renderFluids(ctx);
		float height = Mth.clamp(fluidLevel - .3f * ctx.scale, ctx.yMin, ctx.yMax);
		ctx.ms.pushPose();
		ctx.ms.translate(.5, .2f * ctx.scale, .5);
		Random r = new Random(ctx.pos.hashCode());
		Vec3 baseVector = new Vec3(ctx.radius * ctx.scale, height, 0);

		List<ItemStack> list = ctx.tile.getContents().stream().filter(e -> e instanceof TileInfoOverlay.ItemDrawable)
				.map(e -> ((TileInfoOverlay.ItemDrawable) e).stack()).filter(e -> !e.isEmpty()).toList();
		int itemCount = list.size();

		if (itemCount == 1)
			baseVector = new Vec3(0, height, 0);

		float anglePartition = 360f / itemCount;
		for (ItemStack stack : list) {
			ctx.ms.pushPose();
			if (fluidLevel > 0) {
				ctx.ms.translate(0, (Mth.sin(AnimationTickHolder.getRenderTime(ctx.level) / 12f +
						anglePartition * itemCount) + 1.5f) * 1 / 32f, 0);
			}
			Vec3 itemPosition = VecHelper.rotate(baseVector, anglePartition * itemCount, Direction.Axis.Y);
			ctx.ms.translate(itemPosition.x, itemPosition.y, itemPosition.z);
			ctx.ms.mulPose(Vector3f.YP.rotationDegrees(anglePartition * itemCount + 35));
			ctx.ms.mulPose(Vector3f.XP.rotationDegrees(65));
			for (int i = 0; i <= stack.getCount() / 8; i++) {
				ctx.ms.pushPose();
				Vec3 vec = VecHelper.offsetRandomly(Vec3.ZERO, r, 1 / 16f);
				ctx.ms.translate(vec.x, vec.y, vec.z);
				ctx.ms.scale(ctx.scale, ctx.scale, ctx.scale);
				renderItem(ctx, stack);
				ctx.ms.popPose();
			}
			ctx.ms.popPose();
			itemCount--;
		}
		ctx.ms.popPose();
	}

	private static void renderItem(Context ctx, ItemStack stack) {
		Minecraft.getInstance()
				.getItemRenderer()
				.renderStatic(stack, ItemTransforms.TransformType.GROUND, ctx.light, ctx.overlay, ctx.ms, ctx.buffer, 0);
	}

	private static float renderFluids(Context ctx) {
		if (ctx.max_fluid == 0)
			return 0;
		List<FluidStack> list = ctx.tile.getContents().stream()
				.filter(e -> e instanceof TileInfoOverlay.FluidDrawable draw && !draw.stack().isEmpty())
				.map(e -> ((TileInfoOverlay.FluidDrawable) e).stack()).toList();

		int totalUnits = list.stream().reduce(0, (a, stack) -> a + stack.getAmount(), Integer::sum);
		if (totalUnits < 1) return 0;
		float fluidLevel = Mth.clamp(1.0f * totalUnits / ctx.max_fluid, 1e-3f, 1);
		fluidLevel = 1 - (1 - fluidLevel) * (1 - fluidLevel);
		float y = ctx.yMin + (ctx.yMax - ctx.yMin) * fluidLevel;
		float x = ctx.xMin;
		for (FluidStack stack : list) {
			float dx = (ctx.xMax - ctx.xMin) * stack.getAmount() / totalUnits;
			FluidRenderer.renderFluidBox(stack, x, ctx.yMin, ctx.zMin, x + dx, y, ctx.zMax,
					ctx.buffer, ctx.ms, ctx.light, false);
			x += dx;
		}
		return y;
	}
}
