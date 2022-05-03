package dev.xkmc.cuisine.content.tools.mortar;

import dev.xkmc.l2library.block.impl.BlockEntityBlockMethodImpl;
import dev.xkmc.l2library.block.one.BlockEntityBlockMethod;
import dev.xkmc.l2library.block.one.ShapeBlockMethod;
import dev.xkmc.cuisine.init.registrate.CuisineBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class MortarBlock implements ShapeBlockMethod {

	public static final BlockEntityBlockMethod<MortarBlockEntity> TE = new BlockEntityBlockMethodImpl<>(CuisineBlocks.TE_MORTAR, MortarBlockEntity.class);

	private static final VoxelShape SHAPE = Block.box(3D, 0D, 3D, 13.0D, 5.0D, 13.0D);

	@Nullable
	@Override
	public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext ctx) {
		return SHAPE;
	}
}
