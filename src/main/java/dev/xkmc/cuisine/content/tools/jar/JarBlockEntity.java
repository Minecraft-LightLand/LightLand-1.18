package dev.xkmc.cuisine.content.tools.jar;

import dev.xkmc.l2library.block.TickableBlockEntity;
import dev.xkmc.l2library.serial.SerialClass;
import dev.xkmc.cuisine.content.tools.base.RecipeContainer;
import dev.xkmc.cuisine.content.tools.base.handlers.TimeHandler;
import dev.xkmc.cuisine.content.tools.base.tile.CuisineTankTile;
import dev.xkmc.cuisine.content.tools.base.tile.TimeTile;
import dev.xkmc.cuisine.init.registrate.CuisineRecipes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

@SerialClass
public class JarBlockEntity extends CuisineTankTile<JarBlockEntity> implements TickableBlockEntity, TimeTile {

	public static final int MAX_FLUID = 1000;

	@SerialClass.SerialField(toClient = true)
	private final TimeHandler<JarBlockEntity, JarRecipe> timeHandler = new TimeHandler<>(this, CuisineRecipes.RT_JAR.get());

	public JarBlockEntity(BlockEntityType<JarBlockEntity> type, BlockPos pos, BlockState state) {
		super(type, pos, state, (t) -> new RecipeContainer<>(t, 3).setMax(1).add(t),
				new FluidInfo(1, MAX_FLUID, 50));
	}

	@Override
	public void notifyTile() {
		timeHandler.resetProgress();
		this.setChanged();
		this.sync();
	}

	@Override
	public void tick() {
		timeHandler.tick();
	}

	@Override
	public boolean processing() {
		return timeHandler.processing();
	}
}
