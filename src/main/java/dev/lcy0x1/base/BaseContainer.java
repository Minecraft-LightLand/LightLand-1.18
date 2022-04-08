package dev.lcy0x1.base;

import net.minecraft.util.Mth;
import net.minecraft.world.SimpleContainer;

public class BaseContainer extends SimpleContainer {

	private final int max;

	public BaseContainer(int size, int max) {
		super(size);
		this.max = Mth.clamp(max, 1, 64);
	}

	@Override
	public int getMaxStackSize() {
		return max;
	}

}
