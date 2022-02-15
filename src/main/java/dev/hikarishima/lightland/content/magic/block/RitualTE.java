package dev.hikarishima.lightland.content.magic.block;

import com.lcy0x1.base.block.mult.CreateBlockStateBlockMethod;
import com.lcy0x1.base.block.mult.DefaultStateBlockMethod;
import com.lcy0x1.base.block.mult.OnClickBlockMethod;
import com.lcy0x1.core.util.SerialClass;
import dev.lcy0x1.util.SerialClass;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.World;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;

import static sun.java2d.metal.MTLRenderQueue.sync;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
@SerialClass
public class RitualTE extends SyncedSingleItemTE {

    public RitualTE(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    public boolean canPlaceItemThroughFace(int slot, ItemStack stack, @Nullable Direction dire) {
        return dire == Direction.UP && super.canPlaceItemThroughFace(slot, stack, dire);
    }

    @Override
    public boolean canTakeItemThroughFace(int slot, ItemStack stack, Direction dire) {
        return dire == Direction.DOWN && super.canTakeItemThroughFace(slot, stack, dire);
    }

    @Override
    public int getMaxStackSize() {
        return 1;
    }

    @Override
    public void setChanged() {
        super.setChanged();
        sync();
    }

    @Override
    public boolean isLocked() {
        return getBlockState().getValue(BlockStateProperties.LIT);
    }

    protected void setLocked(boolean bool) {
        if (level != null) {
            level.setBlock(getBlockPos(), getBlockState().setValue(BlockStateProperties.LIT, bool), 3);
        }
    }

    public static class RitualPlace implements OnClickBlockMethod, CreateBlockStateBlockMethod, DefaultStateBlockMethod {

        @Override
        public ActionResultType onClick(BlockState bs, World w, BlockPos pos, PlayerEntity pl, Hand h, BlockRayTraceResult r) {
            if (w.isClientSide()) {
                return ActionResultType.SUCCESS;
            }
            TileEntity te = w.getBlockEntity(pos);
            if (te instanceof RitualTE) {
                RitualTE rte = (RitualTE) te;
                if (!rte.isLocked()) {
                    if (rte.isEmpty()) {
                        if (!pl.getMainHandItem().isEmpty()) {
                            rte.setItem(0, pl.getMainHandItem().split(1));
                        }
                    } else {
                        pl.inventory.placeItemBackInInventory(w, rte.removeItem(0, 1));
                    }
                }
            }
            return ActionResultType.SUCCESS;
        }

        @Override
        public void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
            builder.add(BlockStateProperties.LIT);
        }

        @Override
        public BlockState getDefaultState(BlockState state) {
            return state.setValue(BlockStateProperties.LIT, false);
        }

    }

}
