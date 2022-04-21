package dev.xkmc.cuisine.content.tools.base;

import com.jozufozu.flywheel.util.transform.TransformStack;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import com.simibubi.create.content.contraptions.relays.belt.BeltHelper;
import com.simibubi.create.foundation.fluid.FluidRenderer;
import com.simibubi.create.foundation.utility.AnimationTickHolder;
import com.simibubi.create.foundation.utility.VecHelper;
import dev.xkmc.cuisine.content.tools.base.tile.TileInfoOverlay;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.fluids.FluidStack;

import java.util.List;
import java.util.Random;

public class ContentRenderer {

	public record Context(PoseStack ms, MultiBufferSource buffer, int light, int overlay, float partialTicks,
						  float xMin, float xMax, float yMin, float yMax, float zMin, float zMax, float radius,
						  float scale, boolean tilt,
						  TileInfoOverlay.TileInfoProvider tile, BlockPos pos, Level level, int max_fluid
	) {

	}

	public static void renderDepotContent(Context ctx) {
		float fluidLevel = renderFluids(ctx);
		List<ItemStack> list = ctx.tile.getContents().stream().filter(e -> e instanceof TileInfoOverlay.ItemDrawable)
				.map(e -> ((TileInfoOverlay.ItemDrawable) e).stack()).filter(e -> !e.isEmpty()).toList();
		renderDepotInv(ctx.pos, ctx.partialTicks, ctx.ms, ctx.buffer, ctx.light, ctx.overlay, list);
	}

	public static void renderBasinContent(Context ctx) {
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
			if (ctx.tilt) ctx.ms.mulPose(Vector3f.XP.rotationDegrees(65));
			for (int i = 0; i <= stack.getCount() / 8; i++) {
				ctx.ms.pushPose();
				Vec3 vec = VecHelper.offsetRandomly(Vec3.ZERO, r, 1 / 16f);
				ctx.ms.translate(vec.x, vec.y, vec.z);
				ctx.ms.scale(ctx.scale, ctx.scale, ctx.scale);
				renderBasinItem(ctx, stack);
				ctx.ms.popPose();
			}
			ctx.ms.popPose();
			itemCount--;
		}
		ctx.ms.popPose();
	}

	private static void renderBasinItem(Context ctx, ItemStack stack) {
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

	private static void renderDepotInv(BlockPos pos, float partialTicks, PoseStack ms, MultiBufferSource buffer,
									  int light, int overlay, List<ItemStack> list) {

		TransformStack msr = TransformStack.cast(ms);
		Vec3 itemPosition = VecHelper.getCenterOf(pos);

		ms.pushPose();
		ms.translate(.5f, 15 / 16f, .5f);

		// Render output items
		for (int i = 0; i < list.size(); i++) {
			ItemStack stack = list.get(i);
			if (stack.isEmpty())
				continue;
			ms.pushPose();
			msr.nudge(i);

			boolean renderUpright = BeltHelper.isItemUpright(stack);
			msr.rotateY(360 / 8f * i);
			ms.translate(.35f, 0, 0);
			if (renderUpright)
				msr.rotateY(-(360 / 8f * i));
			Random r = new Random(i + 1);
			int angle = (int) (360 * r.nextFloat());
			renderDepotItem(ms, buffer, light, overlay, stack, renderUpright ? angle + 90 : angle, r, itemPosition);
			ms.popPose();
		}

		ms.popPose();
	}

	private static void renderDepotItem(PoseStack ms, MultiBufferSource buffer, int light, int overlay, ItemStack itemStack,
									   int angle, Random r, Vec3 itemPosition) {
		ItemRenderer itemRenderer = Minecraft.getInstance()
				.getItemRenderer();
		TransformStack msr = TransformStack.cast(ms);
		int count = Mth.log2(itemStack.getCount()) / 2;
		boolean renderUpright = BeltHelper.isItemUpright(itemStack);
		boolean blockItem = itemRenderer.getModel(itemStack, null, null, 0).isGui3d();

		ms.pushPose();
		msr.rotateY(angle);

		if (renderUpright) {
			Entity renderViewEntity = Minecraft.getInstance().cameraEntity;
			if (renderViewEntity != null) {
				Vec3 positionVec = renderViewEntity.position();
				Vec3 diff = itemPosition.subtract(positionVec);
				float yRot = (float) (Mth.atan2(diff.x, diff.z) + Math.PI);
				ms.mulPose(Vector3f.YP.rotation(yRot));
			}
			ms.translate(0, 3 / 32d, -1 / 16f);
		}

		for (int i = 0; i <= count; i++) {
			ms.pushPose();
			if (blockItem)
				ms.translate(r.nextFloat() * .0625f * i, 0, r.nextFloat() * .0625f * i);
			ms.scale(.5f, .5f, .5f);
			if (!blockItem && !renderUpright) {
				ms.translate(0, -3 / 16f, 0);
				msr.rotateX(90);
			}
			itemRenderer.renderStatic(itemStack, ItemTransforms.TransformType.FIXED, light, overlay, ms, buffer, 0);
			ms.popPose();

			if (!renderUpright) {
				if (!blockItem)
					msr.rotateY(10);
				ms.translate(0, blockItem ? 1 / 64d : 1 / 16d, 0);
			} else
				ms.translate(0, 0, -1 / 16f);
		}

		ms.popPose();
	}

}
