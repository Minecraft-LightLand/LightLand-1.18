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

public class AllDireBlockMethodImpl implements PlacementBlockMethod, CreateBlockStateBlockMethod, MirrorRotateBlockMethod {

    public AllDireBlockMethodImpl() {
    }

    @Override
    public void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(BlockProxy.FACING);
    }

    @Override
    public BlockState getStateForPlacement(BlockState def, BlockPlaceContext context) {
        return def.setValue(BlockProxy.FACING, context.getClickedFace().getOpposite());
    }


    public BlockState rotate(BlockState state, Rotation rot) {
        return state.setValue(BlockProxy.FACING, rot.rotate(state.getValue(BlockProxy.FACING)));
    }

    public BlockState mirror(BlockState state, Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(BlockProxy.FACING)));
    }
}
