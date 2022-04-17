package dev.xkmc.cuisine.content.tools.basin;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import com.simibubi.create.foundation.fluid.FluidRenderer;
import com.simibubi.create.foundation.utility.AnimationTickHolder;
import com.simibubi.create.foundation.utility.VecHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemStackHandler;

import java.util.Random;

public class BasinRenderer implements BlockEntityRenderer<BasinBlockEntity> {

	public BasinRenderer(BlockEntityRendererProvider.Context ctx) {

	}

	@Override
	public void render(BasinBlockEntity basin, float partialTicks, PoseStack ms, MultiBufferSource buffer,
					   int light, int overlay) {

		float fluidLevel = renderFluids(basin, partialTicks, ms, buffer, light, overlay);
		float level = Mth.clamp(fluidLevel - .3f, 1/16f, .6f);

		ms.pushPose();

		BlockPos pos = basin.getBlockPos();
		ms.translate(.5, .2f, .5);

		Random r = new Random(pos.hashCode());
		Vec3 baseVector = new Vec3(.125, level, 0);

		IItemHandlerModifiable inv = basin.itemCapability.orElse(new ItemStackHandler());
		int itemCount = 0;
		for (int slot = 0; slot < inv.getSlots(); slot++)
			if (!inv.getStackInSlot(slot)
					.isEmpty())
				itemCount++;

		if (itemCount == 1)
			baseVector = new Vec3(0, level, 0);

		float anglePartition = 360f / itemCount;
		for (int slot = 0; slot < inv.getSlots(); slot++) {
			ItemStack stack = inv.getStackInSlot(slot);
			if (stack.isEmpty())
				continue;

			ms.pushPose();

			if (fluidLevel > 0) {
				ms.translate(0,
						(Mth.sin(
								AnimationTickHolder.getRenderTime(basin.getLevel()) / 12f + anglePartition * itemCount) + 1.5f)
								* 1 / 32f,
						0);
			}

			Vec3 itemPosition = VecHelper.rotate(baseVector, anglePartition * itemCount, Direction.Axis.Y);
			ms.translate(itemPosition.x, itemPosition.y, itemPosition.z);

			ms.mulPose(Vector3f.YP.rotationDegrees(anglePartition * itemCount + 35));
			ms.mulPose(Vector3f.XP.rotationDegrees(65));

			for (int i = 0; i <= stack.getCount() / 8; i++) {
				ms.pushPose();

				Vec3 vec = VecHelper.offsetRandomly(Vec3.ZERO, r, 1 / 16f);

				ms.translate(vec.x, vec.y, vec.z);
				renderItem(ms, buffer, light, overlay, stack);
				ms.popPose();
			}
			ms.popPose();

			itemCount--;
		}
		ms.popPose();
	}


	protected void renderItem(PoseStack ms, MultiBufferSource buffer, int light, int overlay, ItemStack stack) {
		Minecraft.getInstance()
				.getItemRenderer()
				.renderStatic(stack, ItemTransforms.TransformType.GROUND, light, overlay, ms, buffer, 0);
	}

	protected float renderFluids(BasinBlockEntity basin, float partialTicks, PoseStack ms, MultiBufferSource buffer,
								 int light, int overlay) {
		int totalUnits = basin.fluids.getFluidInTank(0).getAmount();
		if (totalUnits < 1)
			return 0;

		float fluidLevel = Mth.clamp(1.0f * totalUnits / BasinBlockEntity.MAX_FLUID, 1e-3f, 1);

		float xMin = 1 / 16f;
		float xMax = 15 / 16f;
		final float yMin = 1 / 16f;
		final float yMax = yMin + 5 / 16f * fluidLevel;
		final float zMin = 1 / 16f;
		final float zMax = 15 / 16f;
		FluidStack renderedFluid = basin.fluids.getFluidInTank(0);
		if (renderedFluid.isEmpty())
			return 0;
		FluidRenderer.renderFluidBox(renderedFluid, xMin, yMin, zMin, xMax, yMax, zMax, buffer, ms, light, false);
		return yMax;
	}


}
