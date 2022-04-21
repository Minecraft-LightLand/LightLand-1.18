package dev.xkmc.cuisine.content.tools.base.tile;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public interface LitTile {

	LitTile DEFAULT = new LitTile() {
	};

	default void onLit(Level level, BlockPos pos, BlockState state) {
		if (!level.isClientSide())
			level.setBlockAndUpdate(pos, state);
	}

	default void onUnlit(Level level, BlockPos pos, BlockState state) {
		if (!level.isClientSide())
			level.setBlockAndUpdate(pos, state);
	}

}
