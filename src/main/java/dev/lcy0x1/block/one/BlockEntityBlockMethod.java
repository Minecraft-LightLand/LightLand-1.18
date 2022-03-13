package dev.lcy0x1.block.one;

import dev.lcy0x1.block.type.SingletonBlockMethod;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public interface BlockEntityBlockMethod<T extends BlockEntity> extends SingletonBlockMethod {

	BlockEntity createTileEntity(BlockPos pos, BlockState state);

	BlockEntityType<T> getType();

	Class<T> getEntityClass();
}