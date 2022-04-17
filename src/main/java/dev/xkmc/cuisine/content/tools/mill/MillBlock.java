package dev.xkmc.cuisine.content.tools.mill;

import dev.lcy0x1.block.impl.BlockEntityBlockMethodImpl;
import dev.lcy0x1.block.mult.OnClickBlockMethod;
import dev.lcy0x1.block.one.BlockEntityBlockMethod;
import dev.lcy0x1.block.one.RenderShapeBlockMethod;
import dev.xkmc.cuisine.init.data.CuisineTags;
import dev.xkmc.cuisine.init.registrate.CuisineBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;

import java.util.Objects;
import java.util.Random;

public class MillBlock implements OnClickBlockMethod, RenderShapeBlockMethod {

	public static final BlockEntityBlockMethod<MillBlockEntity> TE = new BlockEntityBlockMethodImpl<>(CuisineBlocks.TE_MILL, MillBlockEntity.class);

	@Override
	public InteractionResult onClick(BlockState state, Level level, BlockPos pos, Player pl, InteractionHand h, BlockHitResult hitResult) {
		ItemStack stack = pl.getItemInHand(h);
		MillBlockEntity te = Objects.requireNonNull((MillBlockEntity) level.getBlockEntity(pos));
		if (pl.isShiftKeyDown()) {
			if (!level.isClientSide())
				te.dumpInventory();
			return InteractionResult.SUCCESS;
		}

		if (!stack.isEmpty()) {
			LazyOptional<IFluidHandlerItem> opt = stack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY);
			if (opt.resolve().isPresent()) {
				IFluidHandlerItem item = opt.resolve().get();
				FluidStack fluidStack = item.getFluidInTank(0);
				if (fluidStack.isEmpty() || CuisineTags.AllFluidTags.JAR_ACCEPT.matches(fluidStack.getFluid())) {
					return FluidUtil.interactWithFluidHandler(pl, h, level, pos, hitResult.getDirection()) ? InteractionResult.SUCCESS : InteractionResult.CONSUME;
				}
			}
			ItemStack copy = stack.copy();
			copy.setCount(1);
			ItemStack remain = te.inventory.addItem(copy);
			if (remain.isEmpty()) stack.shrink(1);
			return InteractionResult.SUCCESS;
		}
		if (te.step()) {
			if (level.isClientSide()) {
				Random random = level.getRandom();
				double d0 = pos.getX() + 1 - random.nextFloat() * 0.5F;
				double d1 = pos.getY() + 1 - random.nextFloat() * 0.5F;
				double d2 = pos.getZ() + 1 - random.nextFloat() * 0.5F;
				level.addParticle(ParticleTypes.END_ROD, d0, d1, d2,
						random.nextGaussian() * 0.005D,
						random.nextGaussian() * 0.005D,
						random.nextGaussian() * 0.005D);

			}
		}
		return InteractionResult.PASS;
	}

	@Override
	public RenderShape getRenderShape(BlockState state) {
		return RenderShape.ENTITYBLOCK_ANIMATED;
	}
}
