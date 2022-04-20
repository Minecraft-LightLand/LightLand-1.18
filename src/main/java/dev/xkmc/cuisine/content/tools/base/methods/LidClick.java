package dev.xkmc.cuisine.content.tools.base.methods;

import dev.xkmc.cuisine.content.tools.base.tile.CuisineTankTile;
import dev.xkmc.cuisine.content.tools.base.tile.LidTile;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.BlockHitResult;

public class LidClick<T extends CuisineTankTile<T> & LidTile> implements TileClick<T> {

	@Override
	public InteractionResult click(Level level, BlockState state, BlockPos pos, T tile, ItemStack stack, Player pl, InteractionHand hand, BlockHitResult result) {
		if (pl.isShiftKeyDown())
			return InteractionResult.PASS;
		boolean open = state.getValue(BlockStateProperties.OPEN);
		if (!open || stack.isEmpty() && tile.canChangeLid()) {
			if (!level.isClientSide()) {
				level.setBlockAndUpdate(pos, tile.onLidChange(state, open));
			}
			return InteractionResult.SUCCESS;
		}
		return InteractionResult.PASS;
	}

}
