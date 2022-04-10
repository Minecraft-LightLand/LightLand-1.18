package dev.lcy0x1.base;

import dev.lcy0x1.serial.SerialClass;
import dev.lcy0x1.serial.codec.AliasCollection;
import net.minecraft.core.NonNullList;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BooleanSupplier;
import java.util.function.Predicate;

@SerialClass
public class BaseTank implements IFluidHandler, AliasCollection<FluidStack> {

	private final int size, capacity;
	private final List<BaseContainerListener<BaseContainer>> listeners = new ArrayList<>();

	private Predicate<FluidStack> predicate = e -> true;
	private BooleanSupplier allowExtract = () -> true;

	@SerialClass.SerialField
	public NonNullList<FluidStack> list;

	public BaseTank(int size, int capacity) {
		this.size = size;
		this.capacity = capacity;
		list = NonNullList.withSize(size, FluidStack.EMPTY);
	}

	public BaseTank add(BaseContainerListener<BaseContainer> listener) {
		listeners.add(listener);
		return this;
	}

	public BaseTank setPredicate(Predicate<FluidStack> predicate) {
		this.predicate = predicate;
		return this;
	}

	public BaseTank setExtract(BooleanSupplier allowExtract) {
		this.allowExtract = allowExtract;
		return this;
	}

	@Override
	public int getTanks() {
		return size;
	}

	@NotNull
	@Override
	public FluidStack getFluidInTank(int tank) {
		return list.get(tank);
	}

	@Override
	public int getTankCapacity(int tank) {
		return capacity;
	}

	@Override
	public boolean isFluidValid(int tank, @NotNull FluidStack stack) {
		return true;
	}

	@Override
	public int fill(FluidStack resource, FluidAction action) {
		if (resource.isEmpty()) return 0;
		if (!predicate.test(resource)) return 0;
		int to_fill = resource.getAmount();
		int filled = 0;
		for (int i = 0; i < size; i++) {
			FluidStack stack = list.get(i);
			if (stack.isFluidEqual(resource)) {
				int remain = capacity - stack.getAmount();
				int fill = Math.min(to_fill, remain);
				filled += fill;
				to_fill -= fill;
				if (action == FluidAction.EXECUTE) {
					resource.shrink(fill);
					stack.grow(fill);
				}
			} else if (stack.isEmpty()) {
				int fill = Math.min(to_fill, capacity);
				filled += fill;
				to_fill -= fill;
				if (action == FluidAction.EXECUTE) {
					FluidStack rep = resource.copy();
					rep.setAmount(fill);
					list.set(i, rep);
					resource.shrink(fill);
				}
			}
			if (resource.isEmpty() || to_fill == 0) break;
		}
		if (action == FluidAction.EXECUTE && filled > 0) {
			setChanged();
		}
		return filled;
	}

	@NotNull
	@Override
	public FluidStack drain(FluidStack resource, FluidAction action) {
		if (resource.isEmpty()) return resource;
		if (!allowExtract.getAsBoolean()) return FluidStack.EMPTY;
		int to_drain = resource.getAmount();
		int drained = 0;
		for (int i = 0; i < size; i++) {
			FluidStack stack = list.get(i);
			if (stack.isFluidEqual(resource)) {
				int remain = stack.getAmount();
				int drain = Math.min(to_drain, remain);
				drained += drain;
				to_drain -= drain;
				if (action == FluidAction.EXECUTE) {
					stack.shrink(drain);
				}
			}
			if (to_drain == 0) break;
		}
		if (action == FluidAction.EXECUTE && drained > 0) {
			setChanged();
		}
		FluidStack ans = resource.copy();
		ans.setAmount(drained);
		return ans;
	}

	@NotNull
	@Override
	public FluidStack drain(int maxDrain, FluidAction action) {
		if (!allowExtract.getAsBoolean()) return FluidStack.EMPTY;
		FluidStack ans = null;
		int to_drain = maxDrain;
		int drained = 0;
		for (int i = 0; i < size; i++) {
			FluidStack stack = list.get(i);
			if (!stack.isEmpty() && (ans == null || stack.isFluidEqual(ans))) {
				int remain = stack.getAmount();
				int drain = Math.min(to_drain, remain);
				drained += drain;
				to_drain -= drain;
				if (ans == null) {
					ans = stack.copy();
				}
				if (action == FluidAction.EXECUTE) {
					stack.shrink(drain);
				}
			}
			if (to_drain == 0) break;
		}
		if (action == FluidAction.EXECUTE && drained > 0) {
			setChanged();
		}
		if (ans == null) {
			return FluidStack.EMPTY;
		}
		ans.setAmount(drained);
		return ans;
	}

	public void setChanged() {
		listeners.forEach(e -> e.notifyTile(null));
	}

	@Override
	public List<FluidStack> getAsList() {
		return list;
	}

	@Override
	public void clear() {
		list.clear();
	}

	@Override
	public void set(int n, int i, FluidStack elem) {
		list.set(i, elem);
	}

	@Override
	public Class<FluidStack> getElemClass() {
		return FluidStack.class;
	}

	public boolean isEmpty() {
		for (FluidStack stack : list) {
			if (!stack.isEmpty())
				return false;
		}
		return true;
	}
}
