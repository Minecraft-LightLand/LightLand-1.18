package dev.xkmc.lightland.content.archery.feature.types;

import dev.xkmc.l2library.util.ServerOnly;
import dev.xkmc.lightland.content.archery.feature.BowArrowFeature;
import dev.xkmc.lightland.content.common.entity.GenericArrowEntity;
import net.minecraft.world.entity.player.Player;

import java.util.function.Consumer;

public interface OnShootFeature extends BowArrowFeature {

	@ServerOnly
	boolean onShoot(Player player, Consumer<Consumer<GenericArrowEntity>> entity);

	default void onClientShoot(GenericArrowEntity entity) {
	}
}
