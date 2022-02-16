package dev.lcy0x1.block.impl;

import com.tterrag.registrate.util.entry.BlockEntityEntry;
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

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class BlockEntityBlockMethodImpl<T extends BlockEntity> implements BlockEntityBlockMethod<T>, OnClickBlockMethod {

    private final BlockEntityEntry<T> type;
    private final Class<T> cls;

    public BlockEntityBlockMethodImpl(BlockEntityEntry<T> type, Class<T> cls) {
        this.type = type;
        this.cls = cls;
    }

    @Override
    public BlockEntity createTileEntity(BlockPos pos, BlockState state) {
        return type.create(pos, state);
    }

    @Override
    public BlockEntityType<T> getType() {
        return type.get();
    }

    @Override
    public Class<T> getEntityClass() {
        return cls;
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
