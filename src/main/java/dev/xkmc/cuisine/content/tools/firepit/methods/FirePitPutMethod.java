package dev.xkmc.cuisine.content.tools.firepit.methods;

import dev.xkmc.l2library.block.mult.OnClickBlockMethod;
import dev.xkmc.cuisine.init.registrate.CuisineBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.BlockHitResult;

public class FirePitPutMethod implements OnClickBlockMethod {

	@Override
	public InteractionResult onClick(BlockState state, Level level, BlockPos pos, Player pl, InteractionHand hand, BlockHitResult result) {
		ItemStack stack = pl.getItemInHand(hand);
		if (stack.getItem() == CuisineBlocks.WOK.get().asItem()) {
			if (!level.isClientSide()) {
				stack.shrink(1);
				BlockState wok_state = CuisineBlocks.WOK.get().getStateForPlacement(new BlockPlaceContext(pl, hand, stack, result));
				if (wok_state == null) wok_state = state;
				boolean lit = state.getValue(BlockStateProperties.LIT);
				BlockState next = CuisineBlocks.FIRE_PIT_WOK.getDefaultState().setValue(BlockStateProperties.LIT, lit)
						.setValue(BlockStateProperties.HORIZONTAL_FACING, wok_state.getValue(BlockStateProperties.HORIZONTAL_FACING));
				level.setBlockAndUpdate(pos, next);
			}
			return InteractionResult.SUCCESS;
		}
		if (stack.getItem() == Items.STICK && stack.getCount() >= 3) {
			if (!level.isClientSide()) {
				stack.shrink(3);
				boolean lit = state.getValue(BlockStateProperties.LIT);
				BlockState next = CuisineBlocks.FIRE_PIT_STICK.getDefaultState().setValue(BlockStateProperties.LIT, lit);
				level.setBlockAndUpdate(pos, next);
			}
			return InteractionResult.SUCCESS;
		}
		return InteractionResult.PASS;
	}

}
