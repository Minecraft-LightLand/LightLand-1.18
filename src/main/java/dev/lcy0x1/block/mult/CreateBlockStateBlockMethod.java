package dev.lcy0x1.block.mult;

import dev.lcy0x1.block.type.MultipleBlockMethod;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;

public interface CreateBlockStateBlockMethod extends MultipleBlockMethod {

	void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder);

}