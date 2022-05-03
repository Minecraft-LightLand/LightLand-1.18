package dev.xkmc.cuisine.content.tools.firepit.methods;

import dev.xkmc.l2library.block.one.ShapeBlockMethod;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class FirePitShapeMethod implements ShapeBlockMethod {

	private final VoxelShape shape;

	public FirePitShapeMethod(int height) {
		shape = Block.box(0D, 0D, 0D, 16.0D, height, 16.0D);
	}

	@Nullable
	@Override
	public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext ctx) {
		return shape;
	}
}
