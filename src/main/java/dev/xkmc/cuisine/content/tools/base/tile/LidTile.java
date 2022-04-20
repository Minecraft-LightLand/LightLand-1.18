package dev.xkmc.cuisine.content.tools.base.tile;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

public interface LidTile {

	default boolean canChangeLid() {
		return true;
	}

	default BlockState onLidChange(BlockState state, boolean open_old) {
		return state.setValue(BlockStateProperties.OPEN, !open_old);
	}

}
