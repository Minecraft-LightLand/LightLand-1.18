package dev.xkmc.lightland.content.archery.feature.bow;

import dev.xkmc.l2library.util.GenericItemStack;
import dev.xkmc.lightland.content.archery.feature.types.OnPullFeature;
import dev.xkmc.lightland.content.archery.feature.types.OnShootFeature;
import dev.xkmc.lightland.content.archery.item.GenericBowItem;
import dev.xkmc.lightland.content.common.entity.GenericArrowEntity;
import dev.xkmc.lightland.util.RayTraceUtil;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

import java.util.function.Consumer;

public record EnderShootFeature(int range) implements OnShootFeature, OnPullFeature, IGlowFeature {

	@Override
	public boolean onShoot(Player player, Consumer<Consumer<GenericArrowEntity>> consumer) {
		if (player == null)
			return false;
		Entity target = RayTraceUtil.serverGetTarget(player);
		if (target == null)
			return false;
		consumer.accept(entity -> entity.setPos(target.position().lerp(target.getEyePosition(), 0.5).add(entity.getDeltaMovement().scale(-1))));
		return true;
	}

	@Override
	public void onPull(Player player, GenericItemStack<GenericBowItem> bow) {

	}

	@Override
	public void tickAim(Player player, GenericItemStack<GenericBowItem> bow) {
		RayTraceUtil.clientUpdateTarget(player, range);
	}

	@Override
	public void stopAim(Player player, GenericItemStack<GenericBowItem> bow) {
		RayTraceUtil.TARGET.updateTarget(null);
	}
}
