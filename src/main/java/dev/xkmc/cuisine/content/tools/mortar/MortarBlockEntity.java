package dev.xkmc.cuisine.content.tools.mortar;

import dev.lcy0x1.base.BaseBlockEntity;
import dev.lcy0x1.base.BaseContainer;
import dev.lcy0x1.base.BaseContainerListener;
import dev.lcy0x1.base.TickableBlockEntity;
import dev.lcy0x1.block.BlockContainer;
import dev.lcy0x1.serial.SerialClass;
import dev.xkmc.cuisine.content.tools.base.TileInfoOverlay;
import dev.xkmc.cuisine.init.registrate.CuisineRecipe;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
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
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@SerialClass
public class MortarBlockEntity extends BaseBlockEntity implements TickableBlockEntity,
		BaseContainerListener<BaseContainer>, BlockContainer,
		TileInfoOverlay.TileInfoProvider {

	public class RecipeContainer extends BaseContainer {

		public RecipeContainer(int size) {
			super(size);
		}

		public MortarBlockEntity getTile() {
			return MortarBlockEntity.this;
		}

	}

	@SerialClass.SerialField(toClient = true)
	protected final RecipeContainer inventory = (RecipeContainer) new RecipeContainer(4)
			.setPredicate(stack -> this.inventory.countSpace() > 2).add(this);

	@SerialClass.SerialField(toClient = true)
	protected int max_step, step;
	protected ResourceLocation recipe;

	protected final LazyOptional<IItemHandlerModifiable> itemCapability;

	public MortarBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
		itemCapability = LazyOptional.of(() -> new InvWrapper(inventory));
	}

	@Override
	public void notifyTile(@Nullable BaseContainer cont) {
		this.resetProgress();
		this.setChanged();
		this.sync();
	}

	@Override
	public List<Container> getContainers() {
		return List.of(inventory);
	}

	public void step() {
		if (step > 0 && level != null) {
			step--;
			if (!level.isClientSide && step == 0) {
				completeRecipe();
			}
		}
	}

	public void tick() {
	}

	private void completeRecipe() {
		if (level == null || level.isClientSide) return;

		Optional<MortarRecipe> optional = level.getRecipeManager().getRecipeFor(CuisineRecipe.RT_MORTAR, inventory, level);
		max_step = 0;
		step = 0;
		if (optional.isPresent()) {
			MortarRecipe r = optional.get();
			r.assemble(inventory);
		}
		notifyTile(null);
	}

	public void resetProgress() {
		max_step = 0;
		step = 0;
		recipe = null;
		if (level != null) {
			Optional<MortarRecipe> optional = level.getRecipeManager().getRecipeFor(CuisineRecipe.RT_MORTAR, inventory, level);
			if (optional.isPresent()) {
				MortarRecipe r = optional.get();
				max_step = r.step;
				step = max_step;
				recipe = r.id;
			}
		}
	}

	public void dumpInventory() {
		if (level == null) return;
		Containers.dropContents(level, this.getBlockPos().above(), inventory);
		notifyTile(null);
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

	@Nonnull
	@Override
	public <T> LazyOptional<T> getCapability(Capability<T> cap, @Nullable Direction side) {
		if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
			return itemCapability.cast();
		return super.getCapability(cap, side);
	}

}
