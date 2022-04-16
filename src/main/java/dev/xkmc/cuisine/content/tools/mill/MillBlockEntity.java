package dev.xkmc.cuisine.content.tools.mill;

import dev.lcy0x1.base.*;
import dev.lcy0x1.block.BlockContainer;
import dev.lcy0x1.serial.SerialClass;
import dev.xkmc.cuisine.content.tools.TileInfoOverlay;
import dev.xkmc.cuisine.init.registrate.CuisineRecipe;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.Containers;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.wrapper.InvWrapper;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@SerialClass
public class MillBlockEntity extends BaseBlockEntity implements TickableBlockEntity,
		BaseContainerListener<BaseContainer>, BlockContainer,
		TileInfoOverlay.TileInfoProvider {

	public class RecipeContainer extends BaseContainer {

		public RecipeContainer(int size) {
			super(size);
		}

		public MillBlockEntity getTile() {
			return MillBlockEntity.this;
		}

	}

	public static final int MAX_FLUID = 1000;

	@SerialClass.SerialField(toClient = true)
	protected final RecipeContainer inventory = (RecipeContainer) new RecipeContainer(1).add(this);
	@SerialClass.SerialField(toClient = true)
	protected final BaseTank fluids = new BaseTank(1, MAX_FLUID).add(this);
	@SerialClass.SerialField(toClient = true)
	protected final BaseTank water = new BaseTank(1, MAX_FLUID)
			.setPredicate(e -> e.getFluid() == Fluids.WATER).add(this);

	@SerialClass.SerialField(toClient = true)
	protected int max_step, step;
	@SerialClass.SerialField
	protected ResourceLocation recipe;

	protected final LazyOptional<IItemHandlerModifiable> itemCapability;
	protected final LazyOptional<IFluidHandler> fluidCapability;

	public MillBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
		itemCapability = LazyOptional.of(() -> new InvWrapper(inventory));
		fluidCapability = LazyOptional.of(() -> new CombinedTankWrapper()
				.add(CombinedTankWrapper.Type.INSERT, water)
				.add(CombinedTankWrapper.Type.EXTRACT, fluids)
				.build());
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

	@Override
	public void tick() {
	}

	public void step() {
		if (step > 0 && level != null) {
			step--;
			if (!level.isClientSide && step == 0) {
				Optional<MillRecipe> optional = level.getRecipeManager().getRecipeFor(CuisineRecipe.RT_MILL, inventory, level);
				inventory.clear();
				fluids.clear();
				max_step = 0;
				if (optional.isPresent()) {
					MillRecipe r = optional.get();
					r.assemble(inventory);
				}
				notifyTile(null);
			}
		}
	}

	public void resetProgress() {
		max_step = 0;
		step = 0;
		recipe = null;
		if (level != null) {
			Optional<MillRecipe> optional = level.getRecipeManager().getRecipeFor(CuisineRecipe.RT_MILL, inventory, level);
			if (optional.isPresent()) {
				MillRecipe r = optional.get();
				max_step = r.step;
				step = max_step;
				recipe = r.id;
			}
		}
	}

	public void dumpInventory() {
		if (level == null) return;
		Containers.dropContents(level, this.getBlockPos().above(), inventory);
		fluids.clear();
		notifyTile(null);
	}


	@Override
	public List<TileInfoOverlay.IDrawable> getContents() {
		List<TileInfoOverlay.IDrawable> list = new ArrayList<>();
		for (ItemStack stack : inventory.getAsList()) {
			if (!stack.isEmpty())
				list.add(new TileInfoOverlay.ItemDrawable(stack));
		}
		for (FluidStack stack : water.getAsList()) {
			if (!stack.isEmpty())
				list.add(new TileInfoOverlay.FluidDrawable(stack, MAX_FLUID, 50));
		}
		for (FluidStack stack : fluids.getAsList()) {
			if (!stack.isEmpty())
				list.add(new TileInfoOverlay.FluidDrawable(stack, MAX_FLUID, 50));
		}
		return list;
	}

	@Nonnull
	@Override
	public <T> LazyOptional<T> getCapability(Capability<T> cap, @Nullable Direction side) {
		if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
			return itemCapability.cast();
		if (cap == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
			return fluidCapability.cast();
		return super.getCapability(cap, side);
	}


}
