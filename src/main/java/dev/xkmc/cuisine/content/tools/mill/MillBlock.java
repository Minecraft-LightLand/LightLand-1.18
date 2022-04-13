package dev.xkmc.cuisine.content.tools.mill;

import dev.lcy0x1.block.impl.BlockEntityBlockMethodImpl;
import dev.lcy0x1.block.mult.OnClickBlockMethod;
import dev.lcy0x1.block.one.BlockEntityBlockMethod;
import dev.xkmc.cuisine.init.data.CuisineTags;
import dev.xkmc.cuisine.init.registrate.CuisineBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;

import java.util.Objects;

public class MillBlock implements OnClickBlockMethod {

	public static final BlockEntityBlockMethod<MillBlockEntity> TE = new BlockEntityBlockMethodImpl<>(CuisineBlocks.TE_MILL, MillBlockEntity.class);

	@Override
	public InteractionResult onClick(BlockState bs, Level w, BlockPos pos, Player pl, InteractionHand h, BlockHitResult r) {
		ItemStack stack = pl.getItemInHand(h);
		MillBlockEntity te = Objects.requireNonNull((MillBlockEntity) w.getBlockEntity(pos));
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
				if (fluidStack.isEmpty() || CuisineTags.AllFluidTags.JAR_ACCEPT.matches(fluidStack.getFluid())) {
					return FluidUtil.interactWithFluidHandler(pl, h, w, pos, r.getDirection()) ? InteractionResult.SUCCESS : InteractionResult.CONSUME;
				}
			}
			ItemStack copy = stack.copy();
			copy.setCount(1);
			ItemStack remain = te.inventory.addItem(copy);
			if (remain.isEmpty()) stack.shrink(1);
			return InteractionResult.SUCCESS;
		}
		return InteractionResult.PASS;
	}

}
