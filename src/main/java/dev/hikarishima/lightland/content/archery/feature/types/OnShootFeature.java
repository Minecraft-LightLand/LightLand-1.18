package dev.hikarishima.lightland.content.archery.feature.types;

import dev.hikarishima.lightland.content.common.entity.GenericArrowEntity;
import dev.hikarishima.lightland.content.archery.feature.BowArrowFeature;
import dev.hikarishima.lightland.util.ServerOnly;
import net.minecraft.world.entity.player.Player;

import java.util.function.Consumer;

public interface OnShootFeature extends BowArrowFeature {

    @ServerOnly
    boolean onShoot(Player player, Consumer<Consumer<GenericArrowEntity>> entity);

    default void onClientShoot(GenericArrowEntity entity){}
}
