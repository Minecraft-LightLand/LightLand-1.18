package dev.xkmc.cuisine.content.tools.mill;

import dev.xkmc.l2library.base.BaseTank;
import dev.xkmc.l2library.base.CombinedTankWrapper;
import dev.xkmc.l2library.block.TickableBlockEntity;
import dev.xkmc.l2library.serial.SerialClass;
import dev.xkmc.cuisine.content.tools.base.RecipeContainer;
import dev.xkmc.cuisine.content.tools.base.handlers.StepHandler;
import dev.xkmc.cuisine.content.tools.base.tile.CuisineTile;
import dev.xkmc.cuisine.content.tools.base.tile.StepTile;
import dev.xkmc.cuisine.content.tools.base.tile.TileInfoOverlay;
import dev.xkmc.cuisine.init.registrate.CuisineRecipes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.Containers;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import javax.annotation.Nonnull;
import java.util.List;

@SerialClass
public class MillBlockEntity extends CuisineTile<MillBlockEntity> implements TickableBlockEntity, IAnimatable, StepTile {

	public static final int MAX_FLUID = 1000, ROTATE_TIME = 20, ROTATE_ALLOW = 10;

	private final AnimationFactory manager = new AnimationFactory(this);

	protected int rotate_time;

	@SerialClass.SerialField(toClient = true)
	protected final BaseTank fluids = new BaseTank(1, MAX_FLUID).add(this);
	@SerialClass.SerialField(toClient = true)
	protected final BaseTank water = new BaseTank(1, MAX_FLUID)
			.setPredicate(e -> e.getFluid() == Fluids.WATER).add(this);

	protected final LazyOptional<IFluidHandler> fluidCapability;

	@SerialClass.SerialField(toClient = true)
	private final StepHandler<MillBlockEntity, MillRecipe> stepHandler = new StepHandler<>(this, CuisineRecipes.RT_MILL.get());

	public MillBlockEntity(BlockEntityType<MillBlockEntity> type, BlockPos pos, BlockState state) {
		super(type, pos, state, t -> new RecipeContainer<>(t, 1).add(t));
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
		List<TileInfoOverlay.IDrawable> list = super.getContents();
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
		if (cap == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
			return fluidCapability.cast();
		return super.getCapability(cap, side);
	}

}
