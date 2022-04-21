package dev.xkmc.cuisine.content.tools.firepit;

import dev.lcy0x1.block.TickableBlockEntity;
import dev.xkmc.cuisine.content.tools.base.CookHandler;
import dev.xkmc.cuisine.content.tools.base.RecipeContainer;
import dev.xkmc.cuisine.content.tools.base.tile.BottleResultTile;
import dev.xkmc.cuisine.content.tools.base.tile.CookTile;
import dev.xkmc.cuisine.content.tools.base.tile.CuisineTankTile;
import dev.xkmc.cuisine.content.tools.base.tile.LitTile;
import dev.xkmc.cuisine.init.data.CuisineTags;
import dev.xkmc.cuisine.init.registrate.CuisineRecipes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class FirePitWokBlockEntity extends CuisineTankTile<FirePitWokBlockEntity> implements TickableBlockEntity,
		LitTile, BottleResultTile, CookTile {

	public static final int MAX_FLUID = 8 * 50;

	private final CookHandler<FirePitWokBlockEntity, FirePitWokRecipe> cookHandler = new CookHandler<>(this, CuisineRecipes.RT_WOK.get());

	public FirePitWokBlockEntity(BlockEntityType<FirePitWokBlockEntity> type, BlockPos pos, BlockState state) {
		super(type, pos, state, t -> new RecipeContainer<>(t, 4).setMax(1)
						.setPredicate(CuisineTags.AllItemTags.CAN_COOK::matches).add(t),
				new FluidInfo(4, MAX_FLUID, 50));
		fluids.setClickMax(50)
				.setPredicate(e -> CuisineTags.AllFluidTags.JAR_ACCEPT.matches(e.getFluid()))
				.setExtract(() -> false);
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
