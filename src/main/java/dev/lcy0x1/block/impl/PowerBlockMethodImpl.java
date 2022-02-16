package dev.lcy0x1.block.impl;

import dev.lcy0x1.block.mult.CreateBlockStateBlockMethod;
import dev.lcy0x1.block.mult.DefaultStateBlockMethod;
import dev.lcy0x1.block.one.BlockPowerBlockMethod;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

public class PowerBlockMethodImpl implements CreateBlockStateBlockMethod, BlockPowerBlockMethod, DefaultStateBlockMethod {

    public PowerBlockMethodImpl() {
    }

    @Override
    public void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(BlockStateProperties.POWER);
    }

    @Override
    public int getSignal(BlockState bs, BlockGetter r, BlockPos pos, Direction d) {
        return bs.getValue(BlockStateProperties.POWER);
    }

    @Override
    public BlockState getDefaultState(BlockState state) {
        return state.setValue(BlockStateProperties.POWER, 0);
    }

}
