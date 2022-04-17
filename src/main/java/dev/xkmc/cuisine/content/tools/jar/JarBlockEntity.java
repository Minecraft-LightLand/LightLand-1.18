package dev.xkmc.cuisine.content.tools.jar;

import dev.lcy0x1.base.*;
import dev.lcy0x1.block.BlockContainer;
import dev.lcy0x1.serial.SerialClass;
import dev.xkmc.cuisine.content.tools.TileInfoOverlay;
import dev.xkmc.cuisine.init.data.CuisineTags;
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
public class JarBlockEntity extends BaseBlockEntity implements TickableBlockEntity,
		BaseContainerListener<BaseContainer>, BlockContainer,
		TileInfoOverlay.TileInfoProvider {

	public class RecipeContainer extends BaseContainer {

		public RecipeContainer(int size) {
			super(size);
		}

		public JarBlockEntity getTile() {
			return JarBlockEntity.this;
		}

	}

	public static final int MAX_FLUID = 1000;

	@SerialClass.SerialField(toClient = true)
	protected final RecipeContainer inventory = (RecipeContainer) new RecipeContainer(3).setMax(1).add(this);
	@SerialClass.SerialField(toClient = true)
	protected final BaseTank fluids = new BaseTank(1, MAX_FLUID)
			.setPredicate(e -> CuisineTags.AllFluidTags.JAR_ACCEPT.matches(e.getFluid())).add(this);

	@SerialClass.SerialField(toClient = true)
	protected int max_time, time;
	@SerialClass.SerialField
	protected ResourceLocation recipe;

	protected final LazyOptional<IItemHandlerModifiable> itemCapability;
	protected final LazyOptional<IFluidHandler> fluidCapability;

	public JarBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
		itemCapability = LazyOptional.of(() -> new InvWrapper(inventory));
		fluidCapability = LazyOptional.of(() -> fluids);
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
		if (time > 0 && level != null) {
			time--;
			if (!level.isClientSide && time == 0) {
				Optional<JarRecipe> optional = level.getRecipeManager().getRecipeFor(CuisineRecipe.RT_JAR, inventory, level);
				inventory.clear();
				fluids.clear();
				max_time = 0;
				if (optional.isPresent()) {
					JarRecipe r = optional.get();
					if (!r.result.isEmpty())
						inventory.addItem(r.result.copy());
					if (!r.remain.isEmpty())
						fluids.fill(r.remain.copy(), IFluidHandler.FluidAction.EXECUTE);
				}
				notifyTile(null);
			}
		}
	}

	public void resetProgress() {
		max_time = 0;
		time = 0;
		recipe = null;
		if (level != null) {
			Optional<JarRecipe> optional = level.getRecipeManager().getRecipeFor(CuisineRecipe.RT_JAR, inventory, level);
			if (optional.isPresent()) {
				JarRecipe r = optional.get();
				max_time = r.time;
				time = max_time;
				recipe = r.id;
			}
		}
	}

	public void dumpInventory() {
		if (level == null) return;
		if (!inventory.isEmpty())
			Containers.dropContents(level, this.getBlockPos().above(), inventory);
		else
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
