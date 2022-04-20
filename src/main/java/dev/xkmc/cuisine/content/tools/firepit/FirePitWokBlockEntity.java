package dev.xkmc.cuisine.content.tools.firepit;

import dev.xkmc.cuisine.content.tools.base.RecipeContainer;
import dev.xkmc.cuisine.content.tools.base.tile.CuisineTile;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class FirePitWokBlockEntity extends CuisineTile<FirePitWokBlockEntity> {

	public FirePitWokBlockEntity(BlockEntityType<FirePitWokBlockEntity> type, BlockPos pos, BlockState state) {
		super(type, pos, state, t -> new RecipeContainer<>(t, 4).setMax(1).add(t));
	}

	@Override
	public void notifyTile() {
		setChanged();
		sync();
	}

}
