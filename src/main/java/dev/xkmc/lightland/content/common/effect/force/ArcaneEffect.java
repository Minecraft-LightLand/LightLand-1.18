package dev.xkmc.lightland.content.common.effect.force;

import dev.xkmc.lightland.content.common.effect.ForceEffect;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;

public class ArcaneEffect extends MobEffect implements ForceEffect {

	public ArcaneEffect(MobEffectCategory type, int color) {
		super(type, color);
	}
}
