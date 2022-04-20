package dev.xkmc.cuisine.content.tools.base.tile;

import dev.lcy0x1.base.BaseBlockEntity;
import dev.lcy0x1.base.BaseContainerListener;
import dev.lcy0x1.block.BlockContainer;
import dev.lcy0x1.serial.SerialClass;
import dev.xkmc.cuisine.content.tools.base.RecipeContainer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.function.Function;

@SerialClass
public abstract class CuisineTile<T extends CuisineTile<T>> extends BaseBlockEntity implements
		BaseContainerListener, BlockContainer, TileInfoOverlay.TileInfoProvider {

	@SerialClass.SerialField(toClient = true)
	public final RecipeContainer<T> inventory;

	public CuisineTile(BlockEntityType<T> type, BlockPos pos, BlockState state, Function<T, RecipeContainer<T>> container) {
		super(type, pos, state);
		this.inventory = container.apply(getThis());
	}

	public abstract void dumpInventory();

	public RecipeContainer<T> getMainInventory() {
		return inventory;
	}

	@SuppressWarnings("unchecked")
	public T getThis() {
		return (T) this;
	}

	public boolean canDumpInventory() {
		return true;
	}

}
