package dev.lcy0x1.block.one;

import dev.lcy0x1.block.type.SingletonBlockMethod;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.function.Supplier;

public abstract class BlockEntityBlockMethod<T extends BlockEntity> implements SingletonBlockMethod {

    public final Supplier<BlockEntityType<T>> type;
    public final Class<T> cls;

    public BlockEntityBlockMethod(Supplier<BlockEntityType<T>> type, Class<T> cls){
        this.type = type;
        this.cls = cls;
    }

    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    public abstract BlockEntity createTileEntity(BlockPos pos, BlockState state);

    public BlockEntityType<T> getType(){
        return type.get();
    }

    public Class<T> getEntityClass(){
        return cls;
    }
}