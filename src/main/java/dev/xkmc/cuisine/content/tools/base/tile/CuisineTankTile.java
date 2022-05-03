package dev.xkmc.cuisine.content.tools.base.tile;

import dev.xkmc.l2library.base.BaseTank;
import dev.xkmc.l2library.serial.SerialClass;
import dev.xkmc.cuisine.content.tools.base.RecipeContainer;
import dev.xkmc.cuisine.init.data.CuisineTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.Containers;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.function.Function;

@SerialClass
public abstract class CuisineTankTile<T extends CuisineTankTile<T>> extends CuisineTile<T> {

	public record FluidInfo(int tank, int max, int gran) {

	}

	@SerialClass.SerialField(toClient = true)
	public final BaseTank fluids;

	protected final LazyOptional<IFluidHandler> fluidCapability;

	public final FluidInfo info;

	public CuisineTankTile(BlockEntityType<T> type, BlockPos pos, BlockState state, Function<T, RecipeContainer<T>> container, FluidInfo info) {
		super(type, pos, state, container);
		this.info = info;
		this.fluids = new BaseTank(info.tank, info.max)
				.setPredicate(e -> CuisineTags.AllFluidTags.JAR_ACCEPT.matches(e.getFluid())).add(this);
		fluidCapability = LazyOptional.of(() -> fluids);
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
		List<TileInfoOverlay.IDrawable> list = super.getContents();
		for (FluidStack stack : fluids.getAsList()) {
			if (!stack.isEmpty())
				list.add(new TileInfoOverlay.FluidDrawable(stack, info.max, info.gran));
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
