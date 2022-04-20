package dev.xkmc.cuisine.content.tools.mill;

import dev.lcy0x1.base.BaseTank;
import dev.lcy0x1.base.CombinedTankWrapper;
import dev.lcy0x1.base.TickableBlockEntity;
import dev.lcy0x1.serial.SerialClass;
import dev.xkmc.cuisine.content.tools.base.*;
import dev.xkmc.cuisine.content.tools.base.tile.CuisineTile;
import dev.xkmc.cuisine.content.tools.base.tile.StepTile;
import dev.xkmc.cuisine.content.tools.base.tile.TileInfoOverlay;
import dev.xkmc.cuisine.init.registrate.CuisineRecipes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
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
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

@SerialClass
public class MillBlockEntity extends CuisineTile<MillBlockEntity> implements TickableBlockEntity, IAnimatable, StepTile {

	public static final int MAX_FLUID = 1000, ROTATE_TIME = 20, ROTATE_ALLOW = 10;

	private final AnimationFactory manager = new AnimationFactory(this);

	@SerialClass.SerialField(toClient = true)
	protected final BaseTank fluids = new BaseTank(1, MAX_FLUID).add(this);
	@SerialClass.SerialField(toClient = true)
	protected final BaseTank water = new BaseTank(1, MAX_FLUID)
			.setPredicate(e -> e.getFluid() == Fluids.WATER).add(this);

	protected int rotate_time;

	protected final LazyOptional<IItemHandlerModifiable> itemCapability;
	protected final LazyOptional<IFluidHandler> fluidCapability;

	@SerialClass.SerialField(toClient = true)
	private final StepHandler<MillBlockEntity, MillRecipe> stepHandler = new StepHandler<>(this, CuisineRecipes.RT_MILL);

	public MillBlockEntity(BlockEntityType<MillBlockEntity> type, BlockPos pos, BlockState state) {
		super(type, pos, state, t -> new RecipeContainer<>(t, 1).add(t));
		itemCapability = LazyOptional.of(() -> new InvWrapper(inventory));
		fluidCapability = LazyOptional.of(() -> new CombinedTankWrapper()
				.add(CombinedTankWrapper.Type.INSERT, water)
				.add(CombinedTankWrapper.Type.EXTRACT, fluids)
				.build());
	}

	@Override
	public void registerControllers(AnimationData data) {
		MillAnimationController controller = new MillAnimationController(this);
		data.addAnimationController(controller);
	}

	@Override
	public AnimationFactory getFactory() {
		return manager;
	}

	@Override
	public void notifyTile() {
		stepHandler.resetProgress();
		this.setChanged();
		this.sync();
	}

	@Override
	public List<Container> getContainers() {
		return List.of(inventory);
	}

	@Override
	public void tick() {
		if (rotate_time > 0) {
			rotate_time--;
		}
	}

	public boolean step() {
		if (rotate_time <= ROTATE_ALLOW && stepHandler.step()) {
			rotate_time += ROTATE_TIME;
			return true;
		}
		return false;
	}

	public void dumpInventory() {
		if (level == null) return;
		if (!inventory.isEmpty())
			Containers.dropContents(level, this.getBlockPos().above(), inventory);
		else {
			water.clear();
			fluids.clear();
		}
		notifyTile();
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
