package dev.xkmc.cuisine.content.tools.mortar;

import dev.xkmc.l2library.block.TickableBlockEntity;
import dev.xkmc.l2library.serial.SerialClass;
import dev.xkmc.cuisine.content.tools.base.RecipeContainer;
import dev.xkmc.cuisine.content.tools.base.handlers.StepHandler;
import dev.xkmc.cuisine.content.tools.base.tile.CuisineTile;
import dev.xkmc.cuisine.content.tools.base.tile.StepTile;
import dev.xkmc.cuisine.init.registrate.CuisineRecipes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.wrapper.InvWrapper;

@SerialClass
public class MortarBlockEntity extends CuisineTile<MortarBlockEntity> implements TickableBlockEntity, StepTile {

	@SerialClass.SerialField(toClient = true)
	private final StepHandler<MortarBlockEntity, MortarRecipe> stepHandler = new StepHandler<>(this, CuisineRecipes.RT_MORTAR.get());

	protected final LazyOptional<IItemHandlerModifiable> itemCapability;

	public MortarBlockEntity(BlockEntityType<MortarBlockEntity> type, BlockPos pos, BlockState state) {
		super(type, pos, state, t -> new RecipeContainer<>(t, 4).setPredicate(stack -> t.inventory.countSpace() > 2).add(t));
		itemCapability = LazyOptional.of(() -> new InvWrapper(inventory));
	}

	@Override
	public void notifyTile() {
		stepHandler.resetProgress();
		this.setChanged();
		this.sync();
	}

	public void tick() {
	}

	@Override
	public boolean step() {
		return stepHandler.step();
	}
}
