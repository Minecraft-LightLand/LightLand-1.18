package dev.xkmc.lightland.content.archery.feature.arrow;

import dev.xkmc.lightland.content.archery.feature.types.OnHitFeature;
import dev.xkmc.lightland.content.common.entity.GenericArrowEntity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.phys.BlockHitResult;

public record ExplodeArrowFeature(float radius) implements OnHitFeature {

	@Override
	public void onHitEntity(GenericArrowEntity arrow, LivingEntity target) {
		arrow.level.explode(arrow, arrow.getX(), arrow.getY(), arrow.getZ(), radius, Explosion.BlockInteraction.NONE);
	}

	@Override
	public void onHitBlock(GenericArrowEntity arrow, BlockHitResult result) {
		arrow.level.explode(arrow, result.getLocation().x, result.getLocation().y, result.getLocation().z, radius, Explosion.BlockInteraction.NONE);
		arrow.discard();
	}
}
