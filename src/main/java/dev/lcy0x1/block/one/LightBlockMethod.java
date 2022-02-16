package dev.lcy0x1.block.one;


import dev.lcy0x1.block.type.SingletonBlockMethod;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;

public interface LightBlockMethod extends SingletonBlockMethod {
    int getLightValue(BlockState bs, BlockGetter w, BlockPos pos);
}