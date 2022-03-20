package dev.lcy0x1.block.one;

import dev.lcy0x1.block.type.SingletonBlockMethod;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public interface ShapeBlockMethod extends SingletonBlockMethod {

	VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext ctx);

	VoxelShape getBlockSupportShape(BlockState state, BlockGetter level, BlockPos pos);

	VoxelShape getVisualShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext ctx);

}
