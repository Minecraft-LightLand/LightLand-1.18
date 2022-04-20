package dev.xkmc.cuisine.content.tools.basin;

import dev.lcy0x1.base.BaseTank;
import dev.lcy0x1.base.TickableBlockEntity;
import dev.lcy0x1.serial.SerialClass;
import dev.xkmc.cuisine.content.tools.base.*;
import dev.xkmc.cuisine.content.tools.base.tile.CuisineTile;
import dev.xkmc.cuisine.content.tools.base.tile.StepTile;
import dev.xkmc.cuisine.content.tools.base.tile.TileInfoOverlay;
import dev.xkmc.cuisine.content.tools.base.tile.TimeTile;
import dev.xkmc.cuisine.init.data.CuisineTags;
import dev.xkmc.cuisine.init.registrate.CuisineRecipes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.Container;
import net.minecraft.world.Containers;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
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

@SerialClass
public class BasinBlockEntity extends CuisineTile<BasinBlockEntity> implements TickableBlockEntity, StepTile, TimeTile {

	public static final int MAX_FLUID = 500;

	@SerialClass.SerialField(toClient = true)
	protected final BaseTank fluids = new BaseTank(1, MAX_FLUID)
			.setPredicate(e -> CuisineTags.AllFluidTags.JAR_ACCEPT.matches(e.getFluid())).add(this);

	@SerialClass.SerialField(toClient = true)
	private final StepHandler<BasinBlockEntity, BasinRecipe> stepHandler = new StepHandler<>(this, CuisineRecipes.RT_BASIN);
	@SerialClass.SerialField(toClient = true)
	private final TimeHandler<BasinBlockEntity, BasinDryRecipe> timeHandler = new TimeHandler<>(this, CuisineRecipes.RT_BASIN_DRY);

	protected final LazyOptional<IItemHandlerModifiable> itemCapability;
	protected final LazyOptional<IFluidHandler> fluidCapability;

	public BasinBlockEntity(BlockEntityType<BasinBlockEntity> type, BlockPos pos, BlockState state) {
		super(type, pos, state, t -> new RecipeContainer<>(t, 8).setPredicate(stack -> t.inventory.countSpace() > 4).add(t));
		itemCapability = LazyOptional.of(() -> new InvWrapper(inventory));
		fluidCapability = LazyOptional.of(() -> fluids);
	}

	@Override
	public void notifyTile() {
		stepHandler.resetProgress();
		timeHandler.resetProgress();
		this.setChanged();
		this.sync();
	}

	@Override
	public List<Container> getContainers() {
		return List.of(inventory);
	}

	public boolean step() {
		return stepHandler.step();
	}

	public void tick() {
		if (checkBlockBelow()) {
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

	public void dumpInventory() {
		if (level == null) return;
		if (!inventory.isEmpty())
			Containers.dropContents(level, this.getBlockPos().above(), inventory);
		else
			fluids.clear();
		notifyTile();
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

	@Override
	public boolean processing() {
		return timeHandler.processing();
	}
}
