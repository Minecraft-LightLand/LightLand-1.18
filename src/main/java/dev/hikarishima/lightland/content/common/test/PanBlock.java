package dev.hikarishima.lightland.content.common.test;

import dev.hikarishima.lightland.init.registrate.BlockRegistrate;
import dev.lcy0x1.block.impl.BlockEntityBlockMethodImpl;
import dev.lcy0x1.block.mult.CreateBlockStateBlockMethod;
import dev.lcy0x1.block.mult.DefaultStateBlockMethod;
import dev.lcy0x1.block.mult.OnClickBlockMethod;
import dev.lcy0x1.block.one.BlockEntityBlockMethod;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.FlintAndSteelItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.BlockHitResult;

public class PanBlock implements CreateBlockStateBlockMethod, DefaultStateBlockMethod, OnClickBlockMethod {

	public static final BlockEntityBlockMethod<PanBlockEntity> TE = new BlockEntityBlockMethodImpl<>(BlockRegistrate.TE_PAN, PanBlockEntity.class);

	@Override
	public void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(BlockStateProperties.LIT, BlockStateProperties.SIGNAL_FIRE, BlockStateProperties.OPEN);
	}

	@Override
	public BlockState getDefaultState(BlockState state) {
		return state.setValue(BlockStateProperties.LIT, false).setValue(BlockStateProperties.SIGNAL_FIRE, false).setValue(BlockStateProperties.OPEN, false);
	}

	@Override
	public InteractionResult onClick(BlockState bs, Level w, BlockPos pos, Player pl, InteractionHand h, BlockHitResult r) {
		ItemStack stack = pl.getItemInHand(h);
		if (stack.getItem() instanceof FlintAndSteelItem) {
			w.playSound(pl, pos, SoundEvents.FLINTANDSTEEL_USE, SoundSource.BLOCKS, 1.0F, w.getRandom().nextFloat() * 0.4F + 0.8F);
			if (!w.isClientSide()) {
				w.setBlockAndUpdate(pos, bs.setValue(BlockStateProperties.LIT, true));
			}
			stack.hurtAndBreak(1, pl, (player) -> player.broadcastBreakEvent(h));
		} else {
			if (!w.isClientSide()) {
				w.setBlockAndUpdate(pos, bs.setValue(BlockStateProperties.OPEN, !bs.getValue(BlockStateProperties.OPEN)));
			}
		}
		return InteractionResult.SUCCESS;
	}

}
