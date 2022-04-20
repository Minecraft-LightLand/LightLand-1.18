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

public class DumpInventory implements TileClick<CuisineTile<?>> {

	@Override
	public InteractionResult click(Level level, BlockState state, BlockPos pos, CuisineTile<?> tile, ItemStack stack, Player pl, InteractionHand hand, BlockHitResult result) {
		if (tile.canDumpInventory() && pl.isShiftKeyDown()) {
			if (!level.isClientSide()) tile.dumpInventory();
			return InteractionResult.SUCCESS;
		}
		return InteractionResult.PASS;
	}

}
