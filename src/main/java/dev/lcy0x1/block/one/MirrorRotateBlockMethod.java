package dev.lcy0x1.block.one;

import dev.lcy0x1.block.type.SingletonBlockMethod;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;

public interface MirrorRotateBlockMethod extends SingletonBlockMethod {
    BlockState mirror(BlockState state, Mirror mirrorIn);

    BlockState rotate(BlockState state, Rotation rot);
}