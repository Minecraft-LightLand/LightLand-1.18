package dev.xkmc.cuisine.content.tools.base.tile;

import dev.xkmc.l2library.base.BaseBlockEntity;
import dev.xkmc.l2library.base.BaseContainerListener;
import dev.xkmc.l2library.block.BlockContainer;
import dev.xkmc.l2library.serial.SerialClass;
import dev.xkmc.cuisine.content.tools.base.RecipeContainer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.Container;
import net.minecraft.world.Containers;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.wrapper.InvWrapper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

@SerialClass
public abstract class CuisineTile<T extends CuisineTile<T>> extends BaseBlockEntity implements
		BaseContainerListener, BlockContainer, TileInfoOverlay.TileInfoProvider {

	@SerialClass.SerialField(toClient = true)
	public final RecipeContainer<T> inventory;

	protected final LazyOptional<IItemHandlerModifiable> itemCapability;

	public CuisineTile(BlockEntityType<T> type, BlockPos pos, BlockState state, Function<T, RecipeContainer<T>> container) {
		super(type, pos, state);
		this.inventory = container.apply(getThis());
		itemCapability = LazyOptional.of(() -> new InvWrapper(inventory));
	}

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

	public void dumpInventory() {
		if (level == null) return;
		Containers.dropContents(level, this.getBlockPos().above(), inventory);
		notifyTile();
	}

	@Override
	public List<TileInfoOverlay.IDrawable> getContents() {
		List<TileInfoOverlay.IDrawable> list = new ArrayList<>();
		for (ItemStack stack : inventory.getAsList()) {
			if (!stack.isEmpty())
				list.add(new TileInfoOverlay.ItemDrawable(stack));
		}
		return list;
	}

	@Override
	public List<Container> getContainers() {
		return List.of(inventory);
	}

	@Override
	@Nonnull
	public <C> LazyOptional<C> getCapability(@NotNull Capability<C> cap, @Nullable Direction side) {
		if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
			return itemCapability.cast();
		return super.getCapability(cap, side);
	}


}
