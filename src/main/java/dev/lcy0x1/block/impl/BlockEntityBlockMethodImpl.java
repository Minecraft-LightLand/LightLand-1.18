package dev.lcy0x1.block.impl;

import dev.lcy0x1.block.mult.OnClickBlockMethod;
import dev.lcy0x1.block.one.BlockEntityBlockMethod;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.function.Supplier;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class BlockEntityBlockMethodImpl<T extends BlockEntity> extends BlockEntityBlockMethod<T> implements OnClickBlockMethod {

    private final Supplier<T> f;

    public BlockEntityBlockMethodImpl(Supplier<BlockEntityType<T>> type, Class<T> cls, Supplier<T> f) {
        super(type, cls);
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
