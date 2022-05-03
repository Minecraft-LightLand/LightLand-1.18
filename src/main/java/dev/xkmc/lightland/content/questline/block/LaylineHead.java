package dev.xkmc.lightland.content.questline.block;

import dev.xkmc.lightland.init.registrate.LightlandBlocks;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.GrowingPlantHeadBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.Random;

public class LaylineHead extends GrowingPlantHeadBlock {

	public static final VoxelShape SHAPE = Block.box(1.0D, 0.0D, 1.0D, 15.0D, 16.0D, 15.0D);

	public LaylineHead(Properties prop) {
		super(prop, Direction.DOWN, SHAPE, false, 1e-4);
	}

	@Override
	protected int getBlocksToGrowWhenBonemealed(Random random) {
		return random.nextFloat() < 0.1 ? 1 : 0;
	}

	@Override
	protected boolean canGrowInto(BlockState state) {
		return state.isAir();
	}

	@Override
	protected Block getBodyBlock() {
		return LightlandBlocks.LAYROOT_BODY.get();
	}
}
