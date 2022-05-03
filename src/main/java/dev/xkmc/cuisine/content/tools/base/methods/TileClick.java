package dev.xkmc.cuisine.content.tools.base.methods;

import dev.xkmc.l2library.block.mult.OnClickBlockMethod;
import dev.xkmc.cuisine.content.tools.base.tile.CuisineTile;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public interface TileClick<T extends CuisineTile<?>> extends OnClickBlockMethod {

	@SuppressWarnings("unchecked")
	@Override
	default InteractionResult onClick(BlockState state, Level level, BlockPos pos, Player pl, InteractionHand hand, BlockHitResult result) {
		ItemStack stack = pl.getItemInHand(hand);
		if (level.getBlockEntity(pos) instanceof CuisineTile<?> t) {
			T tile = (T) t;
			return click(level, state, pos, tile, stack, pl, hand, result);
		}
		return InteractionResult.PASS;
	}

	InteractionResult click(Level level, BlockState state, BlockPos pos, T tile, ItemStack stack, Player pl, InteractionHand hand, BlockHitResult result);

}
