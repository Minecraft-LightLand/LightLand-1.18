package dev.xkmc.cuisine.content.tools.jar;

import dev.hikarishima.lightland.init.data.AllTags;
import dev.lcy0x1.block.impl.BlockEntityBlockMethodImpl;
import dev.lcy0x1.block.mult.AnimateTickBlockMethod;
import dev.lcy0x1.block.mult.OnClickBlockMethod;
import dev.lcy0x1.block.one.BlockEntityBlockMethod;
import dev.xkmc.cuisine.init.data.CuisineTags;
import dev.xkmc.cuisine.init.registrate.CuisineBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidActionResult;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.items.wrapper.InvWrapper;

import java.util.Objects;
import java.util.Random;

public class JarBlock implements OnClickBlockMethod, AnimateTickBlockMethod {

	public static final BlockEntityBlockMethod<JarBlockEntity> TE = new BlockEntityBlockMethodImpl<>(CuisineBlocks.TE_JAR, JarBlockEntity.class);

	@Override
	public InteractionResult onClick(BlockState bs, Level w, BlockPos pos, Player pl, InteractionHand h, BlockHitResult r) {
		ItemStack stack = pl.getItemInHand(h);
		JarBlockEntity te = Objects.requireNonNull((JarBlockEntity) w.getBlockEntity(pos));
		if (pl.isShiftKeyDown()) {
			if (!w.isClientSide())
				te.dumpInventory();
			return InteractionResult.SUCCESS;
		}

		if (!stack.isEmpty()) {
			LazyOptional<IFluidHandlerItem> opt = stack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY);
			if (opt.resolve().isPresent()) {
				IFluidHandlerItem item = opt.resolve().get();
				FluidStack fluidStack = item.getFluidInTank(0);
				if (fluidStack.isEmpty() || AllTags.AllFluidTags.PAN_ACCEPT.matches(fluidStack.getFluid())) {//TODO tag, test
					return FluidUtil.interactWithFluidHandler(pl, h, w, pos, r.getDirection()) ? InteractionResult.SUCCESS : InteractionResult.CONSUME;
				}
			}
			if (stack.is(CuisineTags.AllItemTags.CAN_COOK.tag)) {
				ItemStack copy = stack.copy();
				copy.setCount(1);
				ItemStack remain = te.inventory.addItem(copy);
				if (remain.isEmpty()) stack.shrink(1);
				return InteractionResult.SUCCESS;
			}
			return InteractionResult.FAIL;
		}
		return InteractionResult.PASS;
	}

	@OnlyIn(Dist.CLIENT)
	@Override
	public void animateTick(BlockState state, Level world, BlockPos pos, Random r) {
		BlockEntity te = world.getBlockEntity(pos);
		if (te instanceof JarBlockEntity jar) {
			if (jar.max_time > 0) {
				double d0 = pos.getX() + 1 - r.nextFloat() * 0.5F;
				double d1 = pos.getY() + 1 - r.nextFloat() * 0.5F;
				double d2 = pos.getZ() + 1 - r.nextFloat() * 0.5F;
				if (r.nextInt(5) == 0) {
					world.addParticle(ParticleTypes.END_ROD, d0, d1, d2, r.nextGaussian() * 0.005D, r.nextGaussian() * 0.005D, r.nextGaussian() * 0.005D);
				}
			}
		}
	}
}
