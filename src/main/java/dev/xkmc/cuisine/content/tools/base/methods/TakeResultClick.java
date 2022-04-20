package dev.xkmc.cuisine.content.tools.base.methods;

import dev.xkmc.cuisine.content.tools.base.CuisineUtil;
import dev.xkmc.cuisine.content.tools.base.tile.BottleResultTile;
import dev.xkmc.cuisine.content.tools.base.tile.CuisineTile;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public class TakeResultClick<T extends CuisineTile<T> & BottleResultTile> implements TileClick<T> {

	@Override
	public InteractionResult click(Level level, BlockState state, BlockPos pos, T tile, ItemStack stack, Player pl, InteractionHand hand, BlockHitResult result) {
		if (!tile.getResult().isEmpty() && tile.canTake()) {
			Ingredient container = CuisineUtil.getContainer(tile.getResult());
			if (container.isEmpty() || container.test(stack)) {
				if (!level.isClientSide()) {
					if (!container.isEmpty())
						stack.shrink(1);
					CuisineUtil.placeBack(pl, tile.getResult().split(1));
					tile.notifyTile();
				}
				return InteractionResult.SUCCESS;
			}
			return InteractionResult.CONSUME;
		}
		return InteractionResult.PASS;
	}
}
