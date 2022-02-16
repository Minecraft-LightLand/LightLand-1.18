package dev.lcy0x1.block.one;

import dev.lcy0x1.block.type.SingletonBlockMethod;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public interface TitleEntityBlockMethod extends SingletonBlockMethod {

    default boolean hasTileEntity(BlockState state) {
        return true;
    }

    BlockEntity createTileEntity(BlockPos pos, BlockState state);
}