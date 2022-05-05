package dev.xkmc.lightland.content.common.effect.force;

import dev.xkmc.l2library.effects.ForceEffect;
import dev.xkmc.lightland.util.DamageUtil;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

public class FlameEffect extends MobEffect implements ForceEffect {

	public FlameEffect(MobEffectCategory type, int color) {
		super(type, color);
	}

	@Override
	public void applyEffectTick(LivingEntity target, int level) {
		DamageUtil.dealDamage(target, DamageSource.IN_FIRE, 2 << level);
	}

	@Override
	public boolean isDurationEffectTick(int tick, int level) {
		return tick % 20 == 0;
	}
}
