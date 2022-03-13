package dev.lcy0x1.block.mult;

import dev.lcy0x1.block.type.MultipleBlockMethod;
import net.minecraft.world.level.block.state.BlockState;

public interface DefaultStateBlockMethod extends MultipleBlockMethod {
	BlockState getDefaultState(BlockState state);
}
