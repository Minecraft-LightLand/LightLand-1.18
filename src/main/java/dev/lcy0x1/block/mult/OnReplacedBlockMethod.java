package dev.lcy0x1.block.mult;

import dev.lcy0x1.block.type.MultipleBlockMethod;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public interface OnReplacedBlockMethod extends MultipleBlockMethod {
    void onReplaced(BlockState state, Level worldIn, BlockPos pos, BlockState newState, boolean isMoving);
}