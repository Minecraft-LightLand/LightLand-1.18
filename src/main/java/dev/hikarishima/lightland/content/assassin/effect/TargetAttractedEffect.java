package dev.hikarishima.lightland.content.assassin.effect;

import dev.hikarishima.lightland.content.common.effect.SkillEffect;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;

public class TargetAttractedEffect extends MobEffect implements SkillEffect {

	public TargetAttractedEffect(MobEffectCategory type, int color) {
		super(type, color);
	}

}
