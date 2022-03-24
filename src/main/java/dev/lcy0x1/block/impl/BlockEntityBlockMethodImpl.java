package dev.lcy0x1.block.impl;

import com.tterrag.registrate.util.entry.BlockEntityEntry;
import dev.lcy0x1.block.NameSetable;
import dev.lcy0x1.block.mult.OnClickBlockMethod;
import dev.lcy0x1.block.mult.SetPlacedByBlockMethod;
import dev.lcy0x1.block.one.BlockEntityBlockMethod;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public record BlockEntityBlockMethodImpl<T extends BlockEntity>(
		BlockEntityEntry<T> type,
		Class<T> cls) implements BlockEntityBlockMethod<T>, OnClickBlockMethod, SetPlacedByBlockMethod {

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

	@Override
	public void setPlacedBy(Level level, BlockPos pos, BlockState state, LivingEntity entity, ItemStack stack) {
		if (stack.hasCustomHoverName()) {
			BlockEntity blockentity = level.getBlockEntity(pos);
			if (blockentity instanceof NameSetable be) {
				be.setCustomName(stack.getHoverName());
			}
		}
	}

}
