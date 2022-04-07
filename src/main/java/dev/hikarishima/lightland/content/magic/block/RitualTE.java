package dev.hikarishima.lightland.content.magic.block;

import dev.lcy0x1.block.mult.CreateBlockStateBlockMethod;
import dev.lcy0x1.block.mult.DefaultStateBlockMethod;
import dev.lcy0x1.block.mult.OnClickBlockMethod;
import dev.lcy0x1.serial.SerialClass;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;

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
		public InteractionResult onClick(BlockState bs, Level w, BlockPos pos, Player pl, InteractionHand h, BlockHitResult r) {
			if (w.isClientSide()) {
				return InteractionResult.SUCCESS;
			}
			BlockEntity te = w.getBlockEntity(pos);
			if (te instanceof RitualTE rte) {
				if (!rte.isLocked()) {
					if (rte.isEmpty()) {
						if (!pl.getMainHandItem().isEmpty()) {
							rte.setItem(0, pl.getMainHandItem().split(1));
						}
					} else {
						pl.getInventory().placeItemBackInInventory(rte.removeItem(0, 1));
					}
				}
			}
			return InteractionResult.SUCCESS;
		}

		@Override
		public void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
			builder.add(BlockStateProperties.LIT);
		}

		@Override
		public BlockState getDefaultState(BlockState state) {
			return state.setValue(BlockStateProperties.LIT, false);
		}

	}

}
