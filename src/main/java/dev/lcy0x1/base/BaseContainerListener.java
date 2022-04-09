package dev.lcy0x1.base;

import net.minecraft.world.Container;
import net.minecraft.world.ContainerListener;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public interface BaseContainerListener<T extends BaseContainer> extends ContainerListener {

	void notifyTile(@Nullable T cont);

	@SuppressWarnings({"unsafe", "unchecked"})
	@Override
	default void containerChanged(Container cont) {
		notifyTile((T) cont);
	}
}
