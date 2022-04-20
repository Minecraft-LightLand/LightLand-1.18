package dev.xkmc.cuisine.content.tools.base.methods;

import dev.xkmc.cuisine.content.tools.pan.PanBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.BlockHitResult;

public class LidClick implements TileClick<PanBlockEntity> {

	@Override
	public InteractionResult click(Level level, BlockState state, BlockPos pos, PanBlockEntity tile, ItemStack stack, Player pl, InteractionHand hand, BlockHitResult result) {
		if (pl.isShiftKeyDown())
			return InteractionResult.PASS;
		boolean open = state.getValue(BlockStateProperties.OPEN);
		boolean lit = state.getValue(BlockStateProperties.LIT);
		boolean cooking = state.getValue(BlockStateProperties.SIGNAL_FIRE);
		if (!open || stack.isEmpty() && tile.result.isEmpty()) {
			if (!level.isClientSide()) {
				BlockState toggle_cap = state.setValue(BlockStateProperties.OPEN, !open);
				if (!open && cooking) {
					toggle_cap = state.setValue(BlockStateProperties.SIGNAL_FIRE, false);
					tile.stopCooking();
				}
				if (open && lit) {
					if (tile.startCooking())
						toggle_cap = state.setValue(BlockStateProperties.SIGNAL_FIRE, true)
								.setValue(BlockStateProperties.OPEN, false);
				}
				level.setBlockAndUpdate(pos, toggle_cap);
			}
			return InteractionResult.SUCCESS;
		}
		return InteractionResult.PASS;
	}

}
