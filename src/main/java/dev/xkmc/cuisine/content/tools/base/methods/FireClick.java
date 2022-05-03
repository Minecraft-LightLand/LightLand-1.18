package dev.xkmc.cuisine.content.tools.base.methods;

import dev.xkmc.l2library.block.mult.CreateBlockStateBlockMethod;
import dev.xkmc.l2library.block.mult.DefaultStateBlockMethod;
import dev.xkmc.l2library.block.mult.OnClickBlockMethod;
import dev.xkmc.l2library.block.one.LightBlockMethod;
import dev.xkmc.cuisine.content.tools.base.CuisineUtil;
import dev.xkmc.cuisine.content.tools.base.tile.LitTile;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.FlintAndSteelItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.BlockHitResult;

public class FireClick implements OnClickBlockMethod, CreateBlockStateBlockMethod, DefaultStateBlockMethod, LightBlockMethod {

	@Override
	public void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(BlockStateProperties.LIT);
	}

	@Override
	public BlockState getDefaultState(BlockState state) {
		return state.setValue(BlockStateProperties.LIT, false);
	}

	@Override
	public int getLightValue(BlockState bs, BlockGetter w, BlockPos pos) {
		return bs.getValue(BlockStateProperties.LIT) ? 15 : 0;
	}

	@Override
	public InteractionResult onClick(BlockState state, Level level, BlockPos pos, Player pl, InteractionHand hand, BlockHitResult result) {
		ItemStack stack = pl.getItemInHand(hand);
		LitTile litTile = LitTile.DEFAULT;
		if (level.getBlockEntity(pos) instanceof LitTile t) {
			litTile = t;
		}
		boolean lit = state.getValue(BlockStateProperties.LIT);
		if (stack.getItem() instanceof FlintAndSteelItem) {
			if (!lit) {
				level.playSound(pl, pos, SoundEvents.FLINTANDSTEEL_USE, SoundSource.BLOCKS, 1.0F, level.getRandom().nextFloat() * 0.4F + 0.8F);
				BlockState next = state.setValue(BlockStateProperties.LIT, true);
				CuisineUtil.hurtAndBreak(pl, stack, hand);
				litTile.onLit(level, pos, next);
				return InteractionResult.SUCCESS;
			} else {
				return InteractionResult.CONSUME;
			}
		}
		if (lit && pl.isShiftKeyDown()) {
			BlockState unlit = state.setValue(BlockStateProperties.LIT, false);
			litTile.onUnlit(level, pos, unlit);
			return InteractionResult.SUCCESS;
		}
		return InteractionResult.PASS;
	}

}
