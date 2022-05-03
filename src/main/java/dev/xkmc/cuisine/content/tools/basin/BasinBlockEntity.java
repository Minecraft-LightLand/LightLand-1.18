package dev.xkmc.cuisine.content.tools.basin;

import dev.xkmc.l2library.block.TickableBlockEntity;
import dev.xkmc.l2library.serial.SerialClass;
import dev.xkmc.cuisine.content.tools.base.RecipeContainer;
import dev.xkmc.cuisine.content.tools.base.handlers.StepHandler;
import dev.xkmc.cuisine.content.tools.base.handlers.TimeHandler;
import dev.xkmc.cuisine.content.tools.base.tile.CuisineTankTile;
import dev.xkmc.cuisine.content.tools.base.tile.StepTile;
import dev.xkmc.cuisine.content.tools.base.tile.TimeTile;
import dev.xkmc.cuisine.init.registrate.CuisineRecipes;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

@SerialClass
public class BasinBlockEntity extends CuisineTankTile<BasinBlockEntity> implements TickableBlockEntity, StepTile, TimeTile {

	public static final int MAX_FLUID = 500;


	@SerialClass.SerialField(toClient = true)
	private final StepHandler<BasinBlockEntity, BasinRecipe> stepHandler = new StepHandler<>(this, CuisineRecipes.RT_BASIN.get());
	@SerialClass.SerialField(toClient = true)
	private final TimeHandler<BasinBlockEntity, BasinDryRecipe> timeHandler = new TimeHandler<>(this, CuisineRecipes.RT_BASIN_DRY.get());

	@SerialClass.SerialField
	private boolean has_fire = false;

	public BasinBlockEntity(BlockEntityType<BasinBlockEntity> type, BlockPos pos, BlockState state) {
		super(type, pos, state, t -> new RecipeContainer<>(t, 8).setPredicate(stack -> t.inventory.countSpace() > 4).add(t),
				new FluidInfo(1, MAX_FLUID, 50));
	}

	@Override
	public void notifyTile() {
		stepHandler.resetProgress();
		timeHandler.resetProgress();
		this.setChanged();
		this.sync();
	}

	public boolean step() {
		return stepHandler.step();
	}

	public void tick() {
		boolean new_fire = checkBlockBelow();
		if (has_fire != new_fire) {
			notifyTile();
			has_fire = new_fire;
		}
		if (new_fire) {
			timeHandler.tick();
		}
	}

	protected boolean checkBlockBelow() {
		if (level == null) return false;
		BlockState state = level.getBlockState(getBlockPos().below());
		return state.is(BlockTags.FIRE) ||
				state.is(BlockTags.CAMPFIRES) &&
						state.hasProperty(BlockStateProperties.LIT) &&
						state.getValue(BlockStateProperties.LIT);
	}

	@Override
	public boolean processing() {
		return timeHandler.processing();
	}
}
