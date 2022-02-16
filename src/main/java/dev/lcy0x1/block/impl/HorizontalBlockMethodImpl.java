package dev.lcy0x1.block.impl;

import dev.lcy0x1.block.BlockProxy;
import dev.lcy0x1.block.mult.CreateBlockStateBlockMethod;
import dev.lcy0x1.block.mult.PlacementBlockMethod;
import dev.lcy0x1.block.one.MirrorRotateBlockMethod;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;

public class HorizontalBlockMethodImpl implements MirrorRotateBlockMethod, CreateBlockStateBlockMethod, PlacementBlockMethod {

    public HorizontalBlockMethodImpl() {
    }

    @Override
    public void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(BlockProxy.HORIZONTAL_FACING);
    }

    @Override
    public BlockState getStateForPlacement(BlockState def, BlockPlaceContext context) {
        return def.setValue(BlockProxy.HORIZONTAL_FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirrorIn) {
        return state.rotate(mirrorIn.getRotation(state.getValue(BlockProxy.HORIZONTAL_FACING)));
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rot) {
        return state.setValue(BlockProxy.HORIZONTAL_FACING, rot.rotate(state.getValue(BlockProxy.HORIZONTAL_FACING)));
    }
}
