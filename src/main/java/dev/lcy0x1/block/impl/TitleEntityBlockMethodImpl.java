package dev.lcy0x1.block.impl;

import dev.lcy0x1.block.mult.OnClickBlockMethod;
import dev.lcy0x1.block.one.TitleEntityBlockMethod;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

import java.util.function.Supplier;

public class TitleEntityBlockMethodImpl implements TitleEntityBlockMethod, OnClickBlockMethod {

    private final Supplier<? extends BlockEntity> f;

    public TitleEntityBlockMethodImpl(Supplier<? extends BlockEntity> f) {
        this.f = f;
    }

    @Override
    public BlockEntity createTileEntity(BlockPos pos, BlockState state) {
        return f.get();
    }

    @Override
    public InteractionResult onClick(BlockState bs, Level w, BlockPos pos, Player pl, InteractionHand h, BlockHitResult r) {
        BlockEntity te = w.getBlockEntity(pos);
        if (w.isClientSide())
            return te instanceof MenuProvider ? InteractionResult.SUCCESS : InteractionResult.PASS;
        if (te instanceof MenuProvider) {
            pl.openMenu((MenuProvider) te);
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }

}
