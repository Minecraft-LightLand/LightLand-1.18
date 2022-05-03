package dev.xkmc.lightland.content.archery.feature.arrow;

import dev.xkmc.lightland.content.archery.feature.types.OnHitFeature;
import dev.xkmc.lightland.content.common.entity.GenericArrowEntity;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.BlockHitResult;

import java.util.function.Function;

public class DamageArrowFeature implements OnHitFeature {

	private final Function<GenericArrowEntity, DamageSource> source;
	private final Function<GenericArrowEntity, Float> damage;

	public DamageArrowFeature(Function<GenericArrowEntity, DamageSource> source, Function<GenericArrowEntity, Float> damage) {
		this.source = source;
		this.damage = damage;
	}

	@Override
	public void onHitEntity(GenericArrowEntity arrow, LivingEntity target) {
		DamageSource source = this.source.apply(arrow);
		float damage = this.damage.apply(arrow);
		target.hurt(source, damage);
	}

	@Override
	public void onHitBlock(GenericArrowEntity genericArrow, BlockHitResult result) {

	}
}
