package dev.xkmc.cuisine.content.tools.base.methods;

import dev.xkmc.cuisine.content.tools.base.tile.CuisineTile;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;

public class AddOneItem implements TileClick<CuisineTile<?>> {

	@Override
	public InteractionResult click(Level level, BlockState state, BlockPos pos, CuisineTile<?> tile, ItemStack stack, Player pl, InteractionHand hand, BlockHitResult result) {
		if (!stack.isEmpty()) {
			LazyOptional<IFluidHandlerItem> opt = stack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY);
			if (opt.resolve().isPresent()) {
				return FluidUtil.interactWithFluidHandler(pl, hand, level, pos, result.getDirection()) ?
						InteractionResult.SUCCESS : InteractionResult.CONSUME;
			}
			ItemStack copy = stack.copy();
			copy.setCount(1);
			ItemStack remain = tile.getMainInventory().addItem(copy);
			if (remain.isEmpty()) {
				stack.shrink(1);
				return InteractionResult.SUCCESS;
			}
			return InteractionResult.CONSUME;
		}
		return InteractionResult.PASS;
	}
}
