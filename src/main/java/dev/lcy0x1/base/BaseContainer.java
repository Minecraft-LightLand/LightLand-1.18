package dev.lcy0x1.base;

import net.minecraft.util.Mth;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;

import java.util.function.Predicate;

public class BaseContainer extends SimpleContainer {

	private int max = 64;
	private Predicate<ItemStack> predicate = e -> true;

	public BaseContainer(int size) {
		super(size);
	}

	public BaseContainer setMax(int max) {
		this.max = Mth.clamp(max, 1, 64);
		return this;
	}

	public BaseContainer setPredicate(Predicate<ItemStack> predicate) {
		this.predicate = predicate;
		return this;
	}

	public BaseContainer add(BaseContainerListener<BaseContainer> t){
		addListener(t);
		return this;
	}

	@Override
	public boolean canPlaceItem(int slot, ItemStack stack) {
		return predicate.test(stack);
	}

	@Override
	public int getMaxStackSize() {
		return max;
	}

}
