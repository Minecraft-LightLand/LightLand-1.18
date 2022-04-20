package dev.xkmc.cuisine.content.tools.pan;

import dev.lcy0x1.block.impl.BlockEntityBlockMethodImpl;
import dev.lcy0x1.block.mult.CreateBlockStateBlockMethod;
import dev.lcy0x1.block.mult.DefaultStateBlockMethod;
import dev.lcy0x1.block.one.BlockEntityBlockMethod;
import dev.xkmc.cuisine.init.registrate.CuisineBlocks;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

public class PanBlock implements CreateBlockStateBlockMethod, DefaultStateBlockMethod {

	public static final BlockEntityBlockMethod<PanBlockEntity> TE = new BlockEntityBlockMethodImpl<>(CuisineBlocks.TE_PAN, PanBlockEntity.class);

	@Override
	public void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(BlockStateProperties.SIGNAL_FIRE, BlockStateProperties.OPEN);
	}

	@Override
	public BlockState getDefaultState(BlockState state) {
		return state.setValue(BlockStateProperties.SIGNAL_FIRE, false).setValue(BlockStateProperties.OPEN, false);
	}

}
