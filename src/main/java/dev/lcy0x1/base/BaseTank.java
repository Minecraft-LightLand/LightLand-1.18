package dev.lcy0x1.base;

import dev.lcy0x1.serial.SerialClass;
import net.minecraft.core.NonNullList;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import org.jetbrains.annotations.NotNull;

@SerialClass
public class BaseTank implements IFluidHandler {

	private final int size, capacity;

	@SerialClass.SerialField
	public NonNullList<FluidStack> list;

	public BaseTank(int size, int capacity) {
		this.size = size;
		this.capacity = capacity;
		list = NonNullList.withSize(size, FluidStack.EMPTY);
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
					resource.shrink(fill);
					FluidStack rep = resource.copy();
					rep.setAmount(fill);
					list.set(i, rep);
				}
			}
			if (to_fill == 0) break;
		}
		if (action == FluidAction.EXECUTE && filled > 0) {
			setChanged();
		}
		return filled;
	}

	@NotNull
	@Override
	public FluidStack drain(FluidStack resource, FluidAction action) {
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
		FluidStack ans = null;
		int to_drain = maxDrain;
		int drained = 0;
		for (int i = 0; i < size; i++) {
			FluidStack stack = list.get(i);
			if (ans == null || stack.isFluidEqual(ans)) {
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

	}

}
