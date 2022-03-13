package dev.lcy0x1.block.mult;

import dev.lcy0x1.block.type.MultipleBlockMethod;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.state.BlockState;

public interface PlacementBlockMethod extends MultipleBlockMethod {

	BlockState getStateForPlacement(BlockState def, BlockPlaceContext context);

}