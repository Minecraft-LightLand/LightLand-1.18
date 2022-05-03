package dev.xkmc.lightland.content.questline.block;

import dev.xkmc.lightland.init.registrate.LightlandBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.GrowingPlantBodyBlock;
import net.minecraft.world.level.block.GrowingPlantHeadBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.VoxelShape;

public class LayrootBody extends GrowingPlantBodyBlock {

	public static final VoxelShape SHAPE = Block.box(1.0D, 0.0D, 1.0D, 15.0D, 16.0D, 15.0D);

	public LayrootBody(Properties p_53886_) {
		super(p_53886_, Direction.DOWN, SHAPE, false);
	}

	public boolean isValidBonemealTarget(BlockGetter p_53900_, BlockPos p_53901_, BlockState p_53902_, boolean p_53903_) {
		return false;
	}

	@Override
	protected GrowingPlantHeadBlock getHeadBlock() {
		return LightlandBlocks.LAYROOT_HEAD.get();
	}

}
