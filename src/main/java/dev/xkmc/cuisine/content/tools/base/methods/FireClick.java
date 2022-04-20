package dev.xkmc.cuisine.content.tools.base.methods;

import dev.lcy0x1.block.mult.CreateBlockStateBlockMethod;
import dev.lcy0x1.block.mult.DefaultStateBlockMethod;
import dev.lcy0x1.block.one.LightBlockMethod;
import dev.xkmc.cuisine.content.tools.base.tile.CuisineTile;
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

public class FireClick<T extends CuisineTile<T> & LitTile> implements TileClick<T>, CreateBlockStateBlockMethod, DefaultStateBlockMethod, LightBlockMethod {

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
	public InteractionResult click(Level level, BlockState state, BlockPos pos, T tile, ItemStack stack, Player pl, InteractionHand hand, BlockHitResult result) {
		boolean lit = state.getValue(BlockStateProperties.LIT);
		if (stack.getItem() instanceof FlintAndSteelItem) {
			if (!lit) {
				level.playSound(pl, pos, SoundEvents.FLINTANDSTEEL_USE, SoundSource.BLOCKS, 1.0F, level.getRandom().nextFloat() * 0.4F + 0.8F);
				BlockState next = state.setValue(BlockStateProperties.LIT, true);
				stack.hurtAndBreak(1, pl, (player) -> player.broadcastBreakEvent(hand));
				tile.onLit(level, pos, next);
				return InteractionResult.SUCCESS;
			} else {
				return InteractionResult.CONSUME;
			}
		}
		if (lit && pl.isShiftKeyDown()) {
			BlockState unlit = state.setValue(BlockStateProperties.LIT, false);
			tile.onUnlit(level, pos, unlit);
			return InteractionResult.SUCCESS;
		}
		return InteractionResult.PASS;
	}

}
