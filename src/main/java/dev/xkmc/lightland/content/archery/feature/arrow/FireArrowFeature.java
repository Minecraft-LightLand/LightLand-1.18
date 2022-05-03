package dev.xkmc.lightland.content.archery.feature.arrow;

import dev.xkmc.lightland.content.archery.feature.types.OnHitFeature;
import dev.xkmc.lightland.content.archery.feature.types.OnShootFeature;
import dev.xkmc.lightland.content.common.entity.GenericArrowEntity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.BlockHitResult;

import java.util.function.Consumer;

public record FireArrowFeature(int time) implements OnShootFeature, OnHitFeature {

	@Override
	public boolean onShoot(Player player, Consumer<Consumer<GenericArrowEntity>> consumer) {
		consumer.accept((e) -> e.setRemainingFireTicks(time));
		return true;
	}

	@Override
	public void onHitEntity(GenericArrowEntity genericArrow, LivingEntity target) {
		target.setRemainingFireTicks(time);
	}

	@Override
	public void onHitBlock(GenericArrowEntity genericArrow, BlockHitResult result) {

	}
}
