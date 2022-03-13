package dev.lcy0x1.block.one;

import dev.lcy0x1.block.type.SingletonBlockMethod;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;

public interface BlockPowerBlockMethod extends SingletonBlockMethod {

	int getSignal(BlockState bs, BlockGetter r, BlockPos pos, Direction d);
}