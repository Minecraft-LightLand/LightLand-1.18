package dev.xkmc.cuisine.content.tools.base.methods;

import dev.xkmc.cuisine.content.tools.base.CuisineUtil;
import dev.xkmc.cuisine.content.tools.base.tile.CuisineTile;
import dev.xkmc.cuisine.content.tools.base.tile.StepTile;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public class StepClick<T extends CuisineTile<T> & StepTile> implements TileClick<T> {

	@Override
	public InteractionResult click(Level level, BlockState state, BlockPos pos, T tile, ItemStack stack, Player pl, InteractionHand hand, BlockHitResult result) {
		if (tile.step()) {
			if (level.isClientSide()) {
				CuisineUtil.spawnParticle(level, pos, level.getRandom());
			}
			return InteractionResult.SUCCESS;
		}
		return InteractionResult.PASS;
	}

}
