package dev.lcy0x1.base;

import dev.lcy0x1.serial.codec.AliasCollection;
import net.minecraft.util.Mth;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;

import java.util.List;
import java.util.function.Predicate;

public class BaseContainer extends SimpleContainer implements AliasCollection<ItemStack> {

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

	public BaseContainer add(BaseContainerListener<BaseContainer> t) {
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

	@Override
	public List<ItemStack> getAsList() {
		return items;
	}

	@Override
	public void clear() {
		super.clearContent();
	}

	@Override
	public void set(int n, int i, ItemStack elem) {
		items.set(i, elem);
	}

	@Override
	public Class<ItemStack> getElemClass() {
		return ItemStack.class;
	}
}
