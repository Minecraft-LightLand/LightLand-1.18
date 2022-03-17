package dev.hikarishima.lightland.content.common.effect.skill;

import dev.hikarishima.lightland.content.common.effect.SkillEffect;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;

public class ArmorBreakerEffect extends MobEffect implements SkillEffect {

	public ArmorBreakerEffect(MobEffectCategory type, int color) {
		super(type, color);
	}
}
