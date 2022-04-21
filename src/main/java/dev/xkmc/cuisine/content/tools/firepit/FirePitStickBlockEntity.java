package dev.xkmc.cuisine.content.tools.firepit;

import dev.lcy0x1.block.TickableBlockEntity;
import dev.xkmc.cuisine.content.tools.base.CookHandler;
import dev.xkmc.cuisine.content.tools.base.RecipeContainer;
import dev.xkmc.cuisine.content.tools.base.tile.BottleResultTile;
import dev.xkmc.cuisine.content.tools.base.tile.CookTile;
import dev.xkmc.cuisine.content.tools.base.tile.CuisineTile;
import dev.xkmc.cuisine.content.tools.base.tile.LitTile;
import dev.xkmc.cuisine.init.registrate.CuisineRecipes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class FirePitStickBlockEntity extends CuisineTile<FirePitStickBlockEntity> implements TickableBlockEntity,
		LitTile, BottleResultTile, CookTile {

	private final CookHandler<FirePitStickBlockEntity, FirePitStickRecipe> cookHandler = new CookHandler<>(this, CuisineRecipes.RT_STICK.get());

	public FirePitStickBlockEntity(BlockEntityType<FirePitStickBlockEntity> type, BlockPos pos, BlockState state) {
		super(type, pos, state, t -> new RecipeContainer<>(t, 4).setMax(1).add(t));
	}

	@Override
	public void notifyTile() {
		setChanged();
		sync();
	}

	@Override
	public void tick() {
		cookHandler.tick();
	}

	@Override
	public ItemStack getResult() {
		return cookHandler.result;
	}

	@Override
	public void clearResult() {
		cookHandler.result = ItemStack.EMPTY;
	}

	@Override
	public boolean canTake() {
		return true;
	}

	@Override
	public void onStopCooking() {

	}

}